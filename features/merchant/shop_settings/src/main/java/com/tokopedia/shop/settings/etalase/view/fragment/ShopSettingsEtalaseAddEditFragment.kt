package com.tokopedia.shop.settings.etalase.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.gm.common.constant.IMG_URL_POWER_MERCHANT_IDLE_POPUP
import com.tokopedia.gm.common.constant.IMG_URL_REGULAR_MERCHANT_POPUP
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingsEtalaseAddEditPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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
    lateinit var userSession: UserSessionInterface


    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"
        private const val PARAM_IS_SUCCESS = "IS_SUCCESS"
        private const val FEATURE_ETALASE = "Etalase"

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
        userSession = UserSession(activity)
    }

    fun saveAddEditEtalase() {
        if (isValid) {
            etalase.name = edit_text_title.text.toString().trim()
            presenter.saveEtalase(etalase, isEdit)
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
            if (throwable is MessageErrorException) {
                if (isIdlePowerMerchant()) {
                    showIdlePowerMerchantBottomSheet(FEATURE_ETALASE)
                } else if (!isPowerMerchant()) {
                    showRegularMerchantBottomSheet(FEATURE_ETALASE)
                } else {
                    ToasterError.make(view, ErrorHandler.getErrorMessage(activity, throwable), BaseToaster.LENGTH_LONG)
                            .setAction(R.string.title_retry) {
                                saveAddEditEtalase()
                            }.show()
                }
            } else {
                ToasterError.make(view, ErrorHandler.getErrorMessage(activity, throwable), BaseToaster.LENGTH_LONG)
                        .setAction(R.string.title_retry) {
                            saveAddEditEtalase()
                        }.show()
            }
        }
    }

    private fun isIdlePowerMerchant(): Boolean {
        return userSession.isPowerMerchantIdle
    }

    private fun isPowerMerchant(): Boolean {
        return userSession.isGoldMerchant
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
        RouteManager.route(context, ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE)
    }

}