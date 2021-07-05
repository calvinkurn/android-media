package com.tokopedia.shop.settings.notes.view.fragment

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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.listener.ShopSettingsNotesAddEditView
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingsNoteAddEditPresenter
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class ShopSettingsNotesAddEditFragment: BaseDaggerFragment(), ShopSettingsNotesAddEditView {

    private var tfTitle: TextFieldUnify? = null
    private var tfDesc: TextFieldUnify? = null
    private var isEdit = false
    private var isReturnablePolicy = false
    private var shopNote = ShopNoteUiModel()

    @Inject lateinit var presenter: ShopSettingsNoteAddEditPresenter

    companion object {
        private const val MAX_TITLE_CHARACTER = 128
        private const val MAX_CONTENT_CHARACTER = 6000

        private const val PARAM_IS_RETURNABLE_POLICY = "IS_RETURNABLE_POLICY"
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_NOTE = "SHOP_NOTE"

        private const val PARAM_IS_SUCCESS = "IS_SUCCESS"

        fun createInstance(isReturnablePolicy: Boolean, isEdit: Boolean, shopNoteModel: ShopNoteUiModel = ShopNoteUiModel()) =
                ShopSettingsNotesAddEditFragment().apply { arguments = Bundle().apply {
                    putParcelable(PARAM_SHOP_NOTE, shopNoteModel)
                    putBoolean(PARAM_IS_RETURNABLE_POLICY, isReturnablePolicy)
                    putBoolean(PARAM_IS_EDIT, isEdit)
                } }
    }
    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ShopSettingsComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_notes_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        arguments?.let {
            isEdit = it.getBoolean(PARAM_IS_EDIT, false)
            isReturnablePolicy = it.getBoolean(PARAM_IS_RETURNABLE_POLICY, false)
            shopNote = it.getParcelable(PARAM_SHOP_NOTE) ?: ShopNoteUiModel()
        }

        if (isReturnablePolicy){
            tfTitle?.textFieldInput?.setText(R.string.title_returnable_policy)
            tfTitle?.textFieldInput?.isEnabled = false
        } else {
            tfTitle?.textFieldInput?.isEnabled = true
        }

        if (isEdit){
            tfTitle?.textFieldInput?.setText(shopNote.title)
            tfDesc?.textFieldInput?.setText(MethodChecker.fromHtmlPreserveLineBreak(shopNote.content))
            tfTitle?.textFieldInput?.text?.length?.let {
                tfTitle?.textFieldInput?.setSelection(it)
            }
            tfDesc?.textFieldInput?.text?.length?.let {
                tfDesc?.textFieldInput?.setSelection(it)
            }
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    fun saveAddEditNote() {
        if (isDataValidToSave()){
            shopNote.title = tfTitle?.textFieldInput?.text?.toString()?.trim()
            shopNote.content = tfDesc?.textFieldInput?.text?.toString()?.trim()?.replace("\n", "<br />");
            shopNote.terms = isReturnablePolicy
            presenter.saveNote(shopNote, isEdit)
        }
    }

    private fun isDataValidToSave(): Boolean {
        var isValid = true
        val title = tfTitle?.textFieldInput?.text.toString().trim()
        when {
            TextUtils.isEmpty(title) -> {
                isValid = false
                tfTitle?.setError(true)
                tfTitle?.setMessage(getString(R.string.shop_notes_title_required))
            }
            title.length > MAX_TITLE_CHARACTER -> {
                isValid = false
                tfTitle?.setError(true)
                tfTitle?.setMessage(getString(R.string.shop_notes_title_max_length_error, MAX_TITLE_CHARACTER))
            }
            else -> {
                tfTitle?.setError(false)
                tfTitle?.setMessage("")
            }
        }

        val content = tfDesc?.textFieldInput?.text.toString().trim()
        when {
            TextUtils.isEmpty(content) -> {
                isValid = false
                tfDesc?.setError(true)
                tfDesc?.setMessage(getString(R.string.shop_notes_content_required))
            }
            content.length > MAX_CONTENT_CHARACTER -> {
                isValid = false
                tfDesc?.setError(true)
                tfDesc?.setMessage(getString(R.string.shop_notes_content_max_length_error, MAX_CONTENT_CHARACTER))
            }
            else -> {
                tfDesc?.setError(false)
                tfDesc?.setMessage("")
            }
        }

        return isValid
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
            view?.let {
                Toaster.make(it, ErrorHandler.getErrorMessage(activity, throwable),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.title_retry), View.OnClickListener {
                    presenter.saveNote(shopNote, isEdit)
                })
            }
    }

    private fun initializeViews() {
        view?.apply {
            tfTitle = findViewById(R.id.text_input_title)
            tfDesc = findViewById(R.id.text_input_desc)
            tfDesc?.textFieldInput?.isSingleLine = false
        }
    }

}