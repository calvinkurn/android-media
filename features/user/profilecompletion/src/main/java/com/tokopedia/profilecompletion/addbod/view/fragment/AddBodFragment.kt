package com.tokopedia.profilecompletion.addbod.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.view.widget.datepicker.OnDateChangedListener
import com.tokopedia.profilecompletion.addbod.viewmodel.AddBodViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_bod.*
import java.util.*
import javax.inject.Inject
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.widget.FrameLayout
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.custom_test.view.datePicker


/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class AddBodFragment: BaseDaggerFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addBodViewModel by lazy { viewModelProvider.get(AddBodViewModel::class.java) }

    private var defaultDate: Calendar = GregorianCalendar(1980, 0, 1)
    private var minDate: Calendar = GregorianCalendar(1900, 0, 1)
    private var maxDate: Calendar = GregorianCalendar(2100, 0, 1)
    private var isDayShown = true

    private lateinit var closeableBottomSheetDialog : CloseableBottomSheetDialog
    private lateinit var chooseDateButton : View
    private var selectedDate : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_bod, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chooseDate.setOnClickListener {
            val viewBottomSheetDialog = View.inflate(context, R.layout.custom_test, null)

            val datePicker = viewBottomSheetDialog.datePicker
            datePicker.setMinDate(minDate.timeInMillis)
            datePicker.setMaxDate(maxDate.timeInMillis)
            datePicker.init(defaultDate.time, isDayShown, object : OnDateChangedListener{
                override fun onDateChanged(date: Date) {

                }

            })

            chooseDateButton = viewBottomSheetDialog.findViewById(R.id.btn_continue)
            chooseDateButton.setOnClickListener {
                selectedDate = formatDateParam( datePicker.getDayOfMonth(),datePicker.getMonth() , datePicker.getYear() )
                val formattedDateView = String.format("%s %s %s", datePicker.getDayOfMonth(),datePicker.getMonth() , datePicker.getYear())
                chooseDate.setText(formattedDateView)
                closeableBottomSheetDialog.dismiss()
            }

            closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            closeableBottomSheetDialog.setCustomContentView(viewBottomSheetDialog, "DatePicker", true)

            val bottomSheet = closeableBottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from<View>(bottomSheet)
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
            })

            closeableBottomSheetDialog.show()

        }

        btnSave.setOnClickListener {
            if(selectedDate.isNotBlank()) {
                showLoading()
                addBodViewModel.editBodUserProfile(selectedDate)
            }
        }

        initObserver()
    }

    private fun formatDateParam(dayOfMonth: Int, month: Int, year: Int): String {
        return String.format("%s-%s-%s", year.toString(), month.toString(), dayOfMonth.toString())
    }

    private fun initObserver(){
        addBodViewModel.editBodUserProfileResponse.observe(this, Observer {
            when (it) {
                is Success -> onSuccessAddBodUserProfile(it.data)
                is Fail -> onErrorAddBodUserProfile(it.throwable)
            }
        })
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun onSuccessAddBodUserProfile(addBodData: AddBodData){
        dismissLoading()
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, addBodData.completionScore)
            bundle.putString(EXTRA_BOD, selectedDate)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun onErrorAddBodUserProfile(throwable: Throwable){
        dismissLoading()
        view?.run{
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }

    }

    private fun showLoading() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    companion object {

        val EXTRA_PROFILE_SCORE = "profile_score"
        val EXTRA_BOD= "bod"

        fun createInstance(bundle: Bundle): AddBodFragment {
            val fragment = AddBodFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}