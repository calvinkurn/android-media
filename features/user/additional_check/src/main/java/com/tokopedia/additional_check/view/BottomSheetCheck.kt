package com.tokopedia.additional_check.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.data.GetObjectPojo
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.AdditionalCheckUseCaseModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import javax.inject.Inject


class BottomSheetCheck(val mActivity: Context?): BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(BottomSheetCheckViewModel::class.java) }

    companion object {
        const val TAG = "BottomSheetCheck"

        fun createInstance(activity: FragmentActivity?): BottomSheetCheck = BottomSheetCheck(activity)
    }

    fun checkStatus(){
        mActivity?.run {
            println("Check status...........")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAdditionalCheckComponents
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .additionalCheckModules(AdditionalCheckModules(context!!))
                .additionalCheckUseCaseModules(AdditionalCheckUseCaseModules())
                .build()
                .inject(this)

        super.onCreate(savedInstanceState)
        initViews()
    }

    fun showBottomSheet(fragmentManager: FragmentManager){
        show(fragmentManager, TAG)
    }

    private fun initObserver(){
        viewModel.getDataResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessCheckData(it.data)
                is Fail -> onFailCheckData(it.throwable)
            }
        })
    }

    private fun onSuccessCheckData(data: GetObjectPojo){

    }

    private fun onFailCheckData(error: Throwable) {

    }

    private fun initViews(){
        val viewBottomSheetDialog = View.inflate(context, R.layout.bottom_sheet_layout, null)

        viewBottomSheetDialog.bs_layout_button.setOnClickListener {

        }

        setChild(viewBottomSheetDialog)
        setCloseClickListener {
            this.dismiss()
        }
    }
}
