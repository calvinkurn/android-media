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
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel
import com.tokopedia.shop.settings.notes.view.listener.ShopSettingsNotesAddEditView
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingsNoteAddEditPresenter
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_shop_notes_add_edit.*
import javax.inject.Inject

class ShopSettingsNotesAddEditFragment: BaseDaggerFragment(), ShopSettingsNotesAddEditView {

    private var isEdit = false
    private var isReturnablePolicy = false
    private var shopNote = ShopNoteViewModel()

    @Inject lateinit var presenter: ShopSettingsNoteAddEditPresenter

    companion object {
        private const val MAX_TITLE_CHARACTER = 128
        private const val MAX_CONTENT_CHARACTER = 6000

        private const val PARAM_IS_RETURNABLE_POLICY = "IS_RETURNABLE_POLICY"
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_NOTE = "SHOP_NOTE"

        private const val PARAM_IS_SUCCESS = "IS_SUCCESS"

        fun createInstance(isReturnablePolicy: Boolean, isEdit: Boolean, shopNoteModel: ShopNoteViewModel = ShopNoteViewModel()) =
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
        arguments?.let {
            isEdit = it.getBoolean(PARAM_IS_EDIT, false)
            isReturnablePolicy = it.getBoolean(PARAM_IS_RETURNABLE_POLICY, false)
            shopNote = it.getParcelable(PARAM_SHOP_NOTE) ?: ShopNoteViewModel()
        }

        if (isReturnablePolicy){
            edit_text_title.setText(R.string.title_returnable_policy)
            edit_text_title.isEnabled = false
        } else {
            edit_text_title.isEnabled = true
        }

        if (isEdit){
            edit_text_title.setText(shopNote.title)
            edit_text_desc.setText(MethodChecker.fromHtmlPreserveLineBreak(shopNote.content))
            edit_text_title.setSelection(edit_text_title.text.length)
            edit_text_desc.setSelection(edit_text_desc.text.length)
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    fun saveAddEditNote() {
        if (isDataValidToSave()){
            shopNote.title = edit_text_title.text.toString().trim()
            shopNote.content = edit_text_desc.text.toString().trim().replace("\n", "<br />");
            shopNote.terms = isReturnablePolicy
            presenter.saveNote(shopNote, isEdit)
        }
    }

    private fun isDataValidToSave(): Boolean {
        var isValid = true
        val title = edit_text_title.text.toString().trim()
        if (TextUtils.isEmpty(title)){
            isValid = false
            text_input_layout_title.error = getString(R.string.shop_notes_title_required)
        } else if (title.length > MAX_TITLE_CHARACTER) {
            isValid = false
            text_input_layout_title.error = getString(R.string.shop_notes_title_max_length_error, MAX_TITLE_CHARACTER)
        }

        val content = edit_text_desc.text.toString().trim()
        if (TextUtils.isEmpty(content)){
            isValid = false
            text_input_layout_desc.error = getString(R.string.shop_notes_content_required)
        } else if (content.length > MAX_CONTENT_CHARACTER) {
            isValid = false
            text_input_layout_desc.error = getString(R.string.shop_notes_content_max_length_error, MAX_CONTENT_CHARACTER)
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
            Toaster.make(view!!, ErrorHandler.getErrorMessage(activity, throwable),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.title_retry), View.OnClickListener {
                presenter.saveNote(shopNote, isEdit)
            })
    }
}