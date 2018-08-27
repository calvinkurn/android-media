package com.tokopedia.shop.settings.etalase.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingsEtalaseAddEditPresenter
import javax.inject.Inject

import kotlinx.android.synthetic.main.fragment_shop_etalase_add_edit.*

class ShopSettingsEtalaseAddEditFragment: BaseDaggerFragment(), ShopSettingsEtalaseAddEditView {
    @Inject lateinit var presenter: ShopSettingsEtalaseAddEditPresenter

    private var isEdit: Boolean = false
    private var etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()
    private var existedEtalase: MutableList<String> = mutableListOf()

    private var isValid = true

    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"
        private const val PARAM_EXISTED_ETALASE = "EXISTED_ETALASE"

        private const val PARAM_IS_SUCCESS = "IS_SUCCESS"

        fun createInstance(isEdit: Boolean, existedEtalase: List<String> = listOf(), etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()) =
                ShopSettingsEtalaseAddEditFragment().apply { arguments = Bundle().apply {
                    putParcelable(PARAM_SHOP_ETALASE, etalase)
                    putStringArrayList(PARAM_EXISTED_ETALASE, ArrayList(existedEtalase))
                    putBoolean(PARAM_IS_EDIT, isEdit)
                } }
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
            existedEtalase = it.getStringArrayList(PARAM_EXISTED_ETALASE)
        }

        if (isEdit){
            existedEtalase.remove(etalase.name)
            edit_text_title.setText(etalase.name)
            edit_text_title.setSelection(edit_text_title.text.length)
        }

        edit_text_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString()
                if (TextUtils.isEmpty(text)){
                    isValid = false
                    text_input_layout_title.error = getString(R.string.shop_etalase_title_required)
                } else if (text in existedEtalase){
                    isValid = false
                    text_input_layout_title.error = getString(R.string.shop_etalase_title_already_exist)
                } else {
                    isValid = true
                    text_input_layout_title.setErrorEnabled(false)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun saveAddEditEtalase() {
        if (isValid){
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
        if (view != null && activity != null)
            ToasterError.make(view, ErrorHandler.getErrorMessage(activity, throwable), BaseToaster.LENGTH_LONG)
                    .setAction(R.string.title_retry){
                        saveAddEditEtalase()
                    }.show()
    }
}