package com.tokopedia.shop.settings.etalase.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.IMG_URL_POWER_MERCHANT_IDLE_POPUP
import com.tokopedia.gm.common.constant.IMG_URL_REGULAR_MERCHANT_POPUP
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.PowerMerchantAccessModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.view.activity.ShopSettingsEtalaseAddEditActivity
import com.tokopedia.shop.settings.etalase.view.bottomsheet.PowerMerchantAccessBottomSheet
import com.tokopedia.shop.settings.etalase.view.viewmodel.ShopSettingsEtalaseAddEditViewModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_etalase_add_edit.*
import javax.inject.Inject

class ShopSettingsEtalaseAddEditFragment : BaseDaggerFragment(),
        PowerMerchantAccessBottomSheet.BottomSheetListener {
    @Inject
    lateinit var viewModel: ShopSettingsEtalaseAddEditViewModel
    private var tfEtalaseLabel: TextFieldUnify? = null
    private var isEdit: Boolean = false
    private var etalase: ShopEtalaseUiModel = ShopEtalaseUiModel()
    private var listEtalaseModel: List<ShopEtalaseModel>? = null
    private var isValid = true

    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"
        private const val PARAM_IS_SUCCESS = "IS_SUCCESS"
        private const val FEATURE_ETALASE = "Etalase"
        private const val DEFAULT_ETALASE_TYPE  = -1
        const val ID = "id"
        const val MAXIMUN_ETALASE_COUNT = 10

        @JvmStatic
        fun createInstance(isEdit: Boolean, etalase: ShopEtalaseUiModel = ShopEtalaseUiModel()) =
                ShopSettingsEtalaseAddEditFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PARAM_SHOP_ETALASE, etalase)
                        putBoolean(PARAM_IS_EDIT, isEdit)
                    }
                }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ShopSettingsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_etalase_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tfEtalaseLabel = view.findViewById(R.id.text_etalase_label)

        arguments?.let {
            isEdit = it.getBoolean(PARAM_IS_EDIT, false)
            etalase = it.getParcelable(PARAM_SHOP_ETALASE) ?: ShopEtalaseUiModel()
        }

        if (isEdit) {
            tfEtalaseLabel?.textFieldInput?.let {
                it.setText(etalase.name)
                it.text?.apply { it.setSelection(length) }
            }
        }

        tfEtalaseLabel?.textFieldInput?.afterTextChanged {
            if (TextUtils.isEmpty(it)) {
                isValid = false
                tfEtalaseLabel?.setError(true)
                tfEtalaseLabel?.setMessage(getString(R.string.shop_etalase_title_required))
            } else {
                isValid = true
                tfEtalaseLabel?.setError(false)
                tfEtalaseLabel?.setMessage("")
            }
        }
        getEtalaseList()
        observeData()
    }

    private fun observeData() {
        observe(viewModel.shopEtalase) { result ->
            hideLoading()
            when(result) {
                is Success -> onSuccessGetEtalaseList(result.data)
                is Fail -> onErrorGetEtalaseList(result.throwable)
            }
        }

        observe(viewModel.saveMessage) { result ->
            hideLoading()
            when(result) {
                is Success -> onSuccesAddEdit(result.data)
                is Fail -> onErrorAddEdit(result.throwable)
            }
        }
    }

    fun saveAddEditEtalase() {
        if (isValid) {
            etalase.name = tfEtalaseLabel?.textFieldInput?.text.toString().trim()
            getEtalaseList()
            if (!isEtalaseDuplicate(etalase.name)) {
                showLoading()
                viewModel.saveShopEtalase(etalase, isEdit)
            } else {
                tfEtalaseLabel?.setError(true)
                tfEtalaseLabel?.setMessage(getString(R.string.shop_etalase_title_already_exist))
            }
        }
    }

    private fun onSuccesAddEdit(string: String?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().putExtras(Bundle().apply {
                putBoolean(PARAM_IS_SUCCESS, !TextUtils.isEmpty(string))
                putBoolean(PARAM_IS_EDIT, isEdit)
            }))
            finish()
        }
    }

    private fun onErrorAddEdit(throwable: Throwable?) {
        if (view != null && activity != null) {
            if (isIdlePowerMerchant() && isEtalaseCountAtMax()) {
                showIdlePowerMerchantBottomSheet(FEATURE_ETALASE)
            } else if (!isPowerMerchant() && isEtalaseCountAtMax()) {
                showRegularMerchantBottomSheet(FEATURE_ETALASE)
            } else {
                showToasterError(throwable) {
                    saveAddEditEtalase()
                }
            }

        }
    }

    private fun onSuccessGetEtalaseList(list: List<ShopEtalaseModel>) {
        scroll_view.visibility = View.VISIBLE
        (activity as? ShopSettingsEtalaseAddEditActivity)?.showSaveButton()
        listEtalaseModel = list
    }

    private fun onErrorGetEtalaseList(throwable: Throwable?) {
        showToasterError(throwable) {
            getEtalaseList()
        }
    }

    private fun showToasterError(throwable: Throwable?, onRetry: () -> Unit) {
        view?.let {
            Toaster.make(it, ErrorHandler.getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR, getString(R.string.title_retry), View.OnClickListener {
                onRetry()
            })
        }
    }

    private fun isIdlePowerMerchant(): Boolean {
        return viewModel.isIdlePowerMerchant()
    }

    private fun isPowerMerchant(): Boolean {
        return viewModel.isPowerMerchant()
    }

    private fun showIdlePowerMerchantBottomSheet(featureName: String) {
        val title = getString(R.string.bottom_sheet_idle_title, featureName)
        val description = getString(R.string.bottom_sheet_idle_desc, featureName)
        val buttonName = getString(R.string.bottom_sheet_idle_btn)
        showBottomSheet(title, IMG_URL_POWER_MERCHANT_IDLE_POPUP, description, buttonName)
    }

    private fun showRegularMerchantBottomSheet(featureName: String) {
        val title = getString(R.string.bottom_sheet_regular_title, featureName)
        val description = getString(R.string.bottom_sheet_regular_desc, featureName)
        val buttonName = getString(R.string.bottom_sheet_regular_btn)
        showBottomSheet(title, IMG_URL_REGULAR_MERCHANT_POPUP, description, buttonName)
    }

    private fun showBottomSheet(title: String, imageUrl: String, description: String, buttonName: String) {
        val model = PowerMerchantAccessModel(
                title,
                description,
                imageUrl,
                buttonName
        )
        val bottomSheet = PowerMerchantAccessBottomSheet.newInstance(model)
        bottomSheet.setListener(this)
        bottomSheet.show(childFragmentManager, "merchant_warning_bottom_sheet")
    }

    override fun onBottomSheetButtonClicked() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
    }

    private fun getEtalaseList() {
        showLoading()
        viewModel.getShopEtalase()
    }

    private fun showLoading() {
        loader?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loader?.visibility = View.GONE
    }

    private fun isEtalaseCountAtMax(): Boolean {
        return listEtalaseModel?.filter { it.type!= DEFAULT_ETALASE_TYPE }?.size ?: 0 >= MAXIMUN_ETALASE_COUNT
    }

    private fun isEtalaseDuplicate(query: String): Boolean {
        return listEtalaseModel?.any { it.name == query } ?: false
    }

}