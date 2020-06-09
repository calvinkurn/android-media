package com.tokopedia.profilecompletion.newprofilecompletion.view.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.viewmodel.AddBodViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.newprofilecompletion.ProfileCompletionNewConstants
import com.tokopedia.profilecompletion.newprofilecompletion.view.util.ProfileCompletionEvents
import com.tokopedia.profilecompletion.newprofilecompletion.view.util.ProfileCompletionProperties
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import rx.Observable
import rx.functions.Action1
import java.lang.RuntimeException
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

/**
 * Created by stevenfredian on 7/3/17.
 */
class ProfileCompletionDateFragment : BaseDaggerFragment() {

    private var month: AutoCompleteTextView? = null
    private var actvContainer: View? = null
    private var year: TextInputEditText? = null
    private var date: TextInputEditText? = null
    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var proceed: View? = null
    private var skip: View? = null
    private var progress: View? = null
    private var position = 0
    private var dateObservable: Observable<String>? = null
    private var yearObservable: Observable<String>? = null
    private var monthObservable: Observable<Int>? = null
    private var selectedDate : String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AddBodViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion_dob, container, false)
        if (profileCompletionFragment != null && profileCompletionFragment?.view != null) {
            initView(parentView)
            setViewListener()
            initialVar()
            initialObserver()
            setUpFields()
        } else if (activity != null) {
            activity?.finish()
        }
        return parentView
    }

    private fun initView(view: View) {
        date = view.findViewById(R.id.date)
        month = view.findViewById(R.id.month)
        year = view.findViewById(R.id.year)
        actvContainer = view.findViewById(R.id.autoCompleteTextViewContainer)
        proceed = profileCompletionFragment?.view?.findViewById(R.id.proceed)
        skip = profileCompletionFragment?.view?.findViewById(R.id.skip)
        progress = profileCompletionFragment?.view?.findViewById(R.id.progress)
        proceed?.isEnabled = false
        profileCompletionFragment?.canProceed(false)

        val drawable = MethodChecker.getDrawable(activity, R.drawable.profilecompletion_chevron_thin_down)
        drawable.setColorFilter(MethodChecker.getColor(activity, R.color.warm_grey), PorterDuff.Mode.SRC_IN)
        val size = drawable.intrinsicWidth * 0.3
        drawable.setBounds(0, 0, size.toInt(), size.toInt())
        month?.setCompoundDrawables(null, null, drawable, null)
    }

    private fun setViewListener() {
        actvContainer?.setOnClickListener {
            month?.showDropDown()
            KeyboardHandler.DropKeyboard(activity, view)
        }
        month?.setOnClickListener {
            month?.showDropDown()
            KeyboardHandler.DropKeyboard(activity, view)
        }
        month?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, rowId ->
            position = pos + 1
        }
        proceed?.setOnClickListener {
            val dateString = date?.text.toString()
            val yearString = year?.text.toString()

            if(dateString.isNotEmpty() && yearString.isNotEmpty()) {
                selectedDate = formatDateParam(yearString.toInt(), position, dateString.toInt())
            }

            if(selectedDate.isNotEmpty()) {
                context?.let { ctx -> viewModel.editBodUserProfile(ctx, selectedDate) }
            } else {
                profileCompletionFragment?.onFailedEditProfile(getString(R.string.invalid_date))
            }
        }
        skip?.setOnClickListener {
            profileCompletionFragment?.skipView(TAG)
        }
        year?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length == 4) {
                    val theYear = charSequence.toString().toInt()
                    if (theYear < YEAR_MIN) {
                        year!!.setText(YEAR_MIN.toString())
                    } else if (theYear > YEAR_MAX) {
                        year!!.setText(YEAR_MAX.toString())
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun initialVar() {
        val monthsIndo = DateFormatSymbols(Locale.getDefault()).months
        val adapter = ArrayAdapter(activity, androidx.appcompat.R.layout.select_dialog_item_material, monthsIndo)
        month?.setAdapter(adapter)

        dateObservable = date?.let { ProfileCompletionEvents.text(it) }
        yearObservable = year?.let { ProfileCompletionEvents.text(it) }
        monthObservable = month?.let { ProfileCompletionEvents.select(it) }

        val dateMapper = dateObservable?.map { text -> text.trim { it <= ' ' } != "" }
        val yearMapper = yearObservable?.map { text -> text.trim { it <= ' ' } != "" }
        val monthMapper = monthObservable?.map { integer ->
            position = integer
            integer != 0
        }

        val allField = Observable.combineLatest(dateMapper, yearMapper, monthMapper) {
            date, year, month -> date && month && year
        }.map { aBoolean -> aBoolean }

        val onError = Action1 {
            obj: Throwable -> obj.printStackTrace()
        }

        allField.subscribe(proceed?.let { ProfileCompletionProperties.enabledFrom(it) }, onError)
        allField.subscribe(Action1 { aBoolean -> profileCompletionFragment?.canProceed(aBoolean) }, onError)
    }

    private fun initialObserver() {
        viewModel.editBodUserProfileResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it) {
                is Success -> {
                    if(it.data.isSuccess) {
                         profileCompletionFragment?.onSuccessEditProfile(ProfileCompletionNewConstants.EDIT_DOB)
                    } else {
                        if(it.data.birthDateMessage.isNotEmpty()) {
                            profileCompletionFragment?.onFailedEditProfile(it.data.birthDateMessage)
                        } else {
                            profileCompletionFragment?.onFailedEditProfile(ErrorHandler.getErrorMessage(context, RuntimeException()))
                        }
                    }
                }
                is Fail -> { profileCompletionFragment?.onFailedEditProfile(ErrorHandler.getErrorMessage(context, it.throwable))  }
            }
        })
    }

    private fun setUpFields() {}
    override fun getScreenName(): String = ""
    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun formatDateParam(year: Int, month: Int, dayOfMonth: Int): String {
        return String.format("%s-%s-%s", year.toString(), month.toString(), dayOfMonth.toString())
    }

    companion object {
        private const val YEAR_MIN = 1937
        private const val YEAR_MAX = 2007
        const val TAG = "date"

        fun createInstance(view: ProfileCompletionFragment?): ProfileCompletionDateFragment {
            val fragment = ProfileCompletionDateFragment()
            fragment.profileCompletionFragment = view
            return fragment
        }
    }
}