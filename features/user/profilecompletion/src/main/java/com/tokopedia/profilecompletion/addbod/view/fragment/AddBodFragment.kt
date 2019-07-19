package com.tokopedia.profilecompletion.addbod.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.view.widget.datepicker.DatePicker
import com.tokopedia.profilecompletion.addbod.view.widget.datepicker.OnDateChangedListener
import com.tokopedia.profilecompletion.addbod.viewmodel.AddBodViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.custom_test.view.*
import kotlinx.android.synthetic.main.fragment_add_bod.*
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

    private var defaultDate: Calendar = GregorianCalendar(1980, 0, 1)
    private var minDate: Calendar = GregorianCalendar(1900, 0, 1)
    private var maxDate: Calendar = GregorianCalendar(2100, 0, 1)
    private var isDayShown = true

    companion object {
        fun createInstance(bundle: Bundle): AddBodFragment {
            val fragment = AddBodFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

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

            val closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            closeableBottomSheetDialog.setCustomContentView(viewBottomSheetDialog, "DatePicker", true)
            closeableBottomSheetDialog.setCancelable(false)
            closeableBottomSheetDialog.show()
        }

        btnSave.setOnClickListener {  }

        initObserver()
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

    }

    private fun onErrorAddBodUserProfile(throwable: Throwable){

    }

    private fun showLoading() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}