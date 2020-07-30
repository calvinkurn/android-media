package com.tokopedia.shop.settings.etalase.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.gm.common.constant.IMG_URL_POWER_MERCHANT_IDLE_POPUP
import com.tokopedia.gm.common.constant.IMG_URL_REGULAR_MERCHANT_POPUP
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.activity.ShopSettingsEtalaseAddEditActivity
import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingsEtalaseAddEditPresenter
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_shop_etalase_add_edit.*
import javax.inject.Inject

class ShopSettingsEtalaseAddEditFragment : BaseDaggerFragment(),
        ShopSettingsEtalaseAddEditView,
        MerchantCommonBottomSheet.BottomSheetListener {
    @Inject
    lateinit var presenter: ShopSettingsEtalaseAddEditPresenter
    private var isEdit: Boolean = false
    private var etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()

    private var isValid = true

    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"
        private const val PARAM_IS_SUCCESS = "IS_SUCCESS"
        private const val FEATURE_ETALASE = "Etalase"
        const val MAXIMUN_ETALASE_COUNT = 10

        @JvmStatic
        fun createInstance(isEdit: Boolean, etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()) =
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
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_etalase_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            isEdit = it.getBoolean(PARAM_IS_EDIT, false)
            etalase = it.getParcelable(PARAM_SHOP_ETALASE) ?: ShopEtalaseViewModel()
        }

        if (isEdit) {
            edit_text_title.setText(etalase.name)
            edit_text_title.setSelection(edit_text_title.text.length)
        }

        edit_text_title.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString()
                if (TextUtils.isEmpty(text)) {
                    isValid = false
                    text_input_layout_title.error = getString(R.string.shop_etalase_title_required)
                } else {
                    isValid = true
                    text_input_layout_title.setErrorEnabled(false)
                }
            }
        })
        getEtalaseList()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    fun saveAddEditEtalase() {
        if (isValid) {
            etalase.name = edit_text_title.text.toString().trim()
            getEtalaseList()
            if (!presenter.isEtalaseDuplicate(etalase.name)) {
                presenter.saveEtalase(etalase, isEdit)
            } else {
                edit_text_title.error = context?.getString(R.string.shop_etalase_title_already_exist)
            }
        }
    }

    override fun onSuccesAddEdit(string: String?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().putExtras(Bundle().apply {
                putBoolean(PARAM_IS_SUCCESS, !TextUtils.isEmpty(string))
                putBoolean(PARAM_IS_EDIT, isEdit)
            }))
            finish()
        }
    }

    override fun onErrorAddEdit(throwable: Throwable?) {
        if (view != null && activity != null) {
            if (isIdlePowerMerchant() && presenter.isEtalaseCountAtMax()) {
                showIdlePowerMerchantBottomSheet(FEATURE_ETALASE)
            } else if (!isPowerMerchant() && presenter.isEtalaseCountAtMax()) {
                showRegularMerchantBottomSheet(FEATURE_ETALASE)
            } else {
                showToasterError(throwable) {
                    saveAddEditEtalase()
                }
            }

        }
    }

    override fun onSuccessGetEtalaseList() {
        scroll_view.visibility = View.VISIBLE
        (activity as? ShopSettingsEtalaseAddEditActivity)?.showSaveButton()
    }

    override fun onErrorGetEtalaseList(throwable: Throwable?) {
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
        return presenter.isIdlePowerMerchant()
    }

    private fun isPowerMerchant(): Boolean {
        return presenter.isPowerMerchant()
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
        val model = MerchantCommonBottomSheet.BottomSheetModel(
                title,
                description,
                imageUrl,
                buttonName,
                ""
        )
        val bottomSheet = MerchantCommonBottomSheet.newInstance(model)
        bottomSheet.setListener(this)
        bottomSheet.show(childFragmentManager, "merchant_warning_bottom_sheet")
    }

    override fun onBottomSheetButtonClicked() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
    }

    private fun getEtalaseList() {
        presenter.getEtalaseList()
    }

    override fun showLoading() {
        progress_bar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar?.visibility = View.GONE
    }

}