package com.tokopedia.additional_check.view

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject


class BottomSheetCheck(val mActivity: FragmentActivity, val data: TwoFactorResult): BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(BottomSheetCheckViewModel::class.java) }

    companion object {
        const val TAG = "BottomSheetCheck"

        fun showBottomSheetCheck(activity: Activity, data: TwoFactorResult){
//            BottomSheetCheck(activity, data).apply {
//                val viewBottomSheetDialog = View.inflate(mActivity, R.layout.bottom_sheet_additional_check_layout, null)
//                viewBottomSheetDialog.bs_layout_button.setOnClickListener {
//                    RouteManager.route(mActivity, data.bottomSheetModel?.applink)
//                }
//                isCancelable = false
//                showCloseIcon = false
//                isHideable = false
//                viewBottomSheetDialog.bs_layout_title.text = data.bottomSheetModel?.text
//                viewBottomSheetDialog.bs_layout_button.text = data.bottomSheetModel?.btnText
//                setChild(viewBottomSheetDialog)
//                show(activity.fragmentManager, TAG!!)
//            }
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        DaggerAdditionalCheckComponents
//                .builder()
//                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
//                .additionalCheckModules(AdditionalCheckModules(context!!))
//                .additionalCheckUseCaseModules(AdditionalCheckUseCaseModules())
//                .build()
//                .inject(this)

//        super.onCreate(savedInstanceState)
//        initObserver()
//    }

//    private fun initObserver(){
//        viewModel.getDataResponse.observe(this, Observer {
//            when(it){
//                is Success -> onSuccessCheckData(it.data)
//                is Fail -> onFailCheckData(it.throwable)
//            }
//        })
//    }
}
