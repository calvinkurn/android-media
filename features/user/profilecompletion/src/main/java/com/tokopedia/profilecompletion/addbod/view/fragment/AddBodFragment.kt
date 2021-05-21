package com.tokopedia.profilecompletion.addbod.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.view.widget.common.LocaleUtils
import com.tokopedia.profilecompletion.addbod.view.widget.common.LocaleUtils.getCurrentLocale
import com.tokopedia.profilecompletion.addbod.viewmodel.AddBodViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_bod.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class AddBodFragment: BaseDaggerFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addBodViewModel by lazy { viewModelProvider.get(AddBodViewModel::class.java) }

    private var minDate: Calendar = GregorianCalendar(1900, 0, 1)
    private lateinit var maxDate: Calendar
    private lateinit var defaultDate: Calendar

    private var unifyDatePicker: DatePickerUnify? = null
    private lateinit var chooseDateButton : View
    private var selectedDate : String = ""
    private var parentContainer: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_bod, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        maxDate = GregorianCalendar(getCurrentLocale(requireContext()))
        defaultDate = GregorianCalendar(getCurrentLocale(requireContext()))

        initVar()
        initDatepicker()

        chooseDate.textFieldInput.setFocusable(false)
        chooseDate.textFieldInput.setOnClickListener {
            fragmentManager?.run {
                unifyDatePicker?.show(this, TAG)
            }
        }

        btnSave.setOnClickListener {
            if(selectedDate.isNotBlank()) {
                showLoading()
                addBodViewModel.editBodUserProfile(requireContext(), selectedDate)
            }
        }

        initObserver()
        parentContainer
    }

    private fun setChoosenDateFormat(date: String){
        chooseDate.textFieldInput.setText(DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_YYYY_MM_DD,
                DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                date))
    }

    private fun initDatepicker(){
        context?.run {
            unifyDatePicker = DatePickerUnify(this, minDate, defaultDate, maxDate)
        }
        unifyDatePicker?.setTitle(getString(R.string.subtitle_bod_setting_profile))
        unifyDatePicker?.datePickerButton?.setOnClickListener {
            val dateArray = unifyDatePicker?.getDate()
            dateArray?.run {
                if(size >= 3){
                    selectedDate = formatDateParam( dateArray[0],dateArray[1]+1 , dateArray[2] )
                    setChoosenDateFormat(selectedDate)
                }
            }
            unifyDatePicker?.dismiss()
        }
    }

    private fun initVar() {
        val bod = arguments?.getString(ApplinkConstInternalGlobal.PARAM_BOD)
        if(!bod.isNullOrEmpty()){
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", LocaleUtils.getIDLocale())
            defaultDate.time = simpleDateFormat.parse(bod)
            setChoosenDateFormat(bod)
            selectedDate = bod
        }else{
            defaultDate.add(Calendar.YEAR, -17)
        }
    }

    private fun formatDateParam(dayOfMonth: Int, month: Int, year: Int): String {
        return String.format("%s-%s-%s", year.toString(), month.toString(), dayOfMonth.toString())
    }

    private fun initObserver(){
        addBodViewModel.editBodUserProfileResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessAddBodUserProfile(it.data)
                is Fail -> onErrorAddBodUserProfile(it.throwable)
            }
        })
    }

    override fun getScreenName(): String = ""

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

        const val TAG = "addDobFragment"
        val EXTRA_PROFILE_SCORE = "profile_score"
        val EXTRA_BOD= "bod"

        fun createInstance(bundle: Bundle): AddBodFragment {
            val fragment = AddBodFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}