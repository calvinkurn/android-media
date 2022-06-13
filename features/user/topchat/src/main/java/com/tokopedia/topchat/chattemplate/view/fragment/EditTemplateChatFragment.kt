package com.tokopedia.topchat.chattemplate.view.fragment

import android.app.Activity
import android.app.AlertDialog
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import android.widget.TextView
import android.widget.EditText
import javax.inject.Inject
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics
import android.os.Bundle
import com.tokopedia.topchat.common.InboxMessageConstant
import rx.functions.Action1
import android.graphics.PorterDuff
import com.tokopedia.topchat.chattemplate.view.uimodel.EditTemplateResultModel
import android.content.Intent
import android.view.*
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.di.ActivityComponentFactory
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel
import com.tokopedia.topchat.common.util.Events
import rx.Observable

open class EditTemplateChatFragment : BaseDaggerFragment() {
    private var counter: TextView? = null
    private var error: TextView? = null
    private var submit: TextView? = null
    private var editText: EditText? = null
    private var list: ArrayList<String> = arrayListOf()
    private var message: String? = null
    private var counterObservable: Observable<Int>? = null
    private var allowDelete = 0
    private var isSeller: Boolean = false

    @Inject
    lateinit var analytics: ChatTemplateAnalytics

    @Inject
    lateinit var viewModel: EditTemplateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            if (it.getInt(InboxMessageConstant.PARAM_MODE) == TemplateChatFragment.CREATE) {
                setHasOptionsMenu(false)
            }
            isSeller = it.getBoolean(TemplateChatActivity.PARAM_IS_SELLER, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_template, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_organize)
        arguments?.let {
            if (it.getInt(InboxMessageConstant.PARAM_NAV) == 1) {
                allowDelete = DISABLE_DELETE
                item.icon.alpha = DISABLE_DELETE
            } else {
                allowDelete = ENABLE_DELETE
                item.icon.alpha = ENABLE_DELETE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == R.id.action_organize) {
            if (allowDelete == ENABLE_DELETE) {
                analytics.trackDeleteTemplateChat()
                showDialogDelete()
            } else {
                showError(MessageErrorException(getString(R.string.minimum_template_chat_warning)))
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogDelete() {
        AlertDialog.Builder(activity)
            .setTitle(R.string.delete_chat_template)
            .setMessage(R.string.forever_deleted_template)
            .setPositiveButton(R.string.delete) { dialog, _ ->
                analytics.trackConfirmDeleteTemplateChat()
                arguments?.getInt(InboxMessageConstant.PARAM_POSITION)?.let {
                    viewModel.deleteTemplate(it, isSeller)
                }
                dialog.dismiss()
            }
            .setNegativeButton(com.tokopedia.resources.common.R.string.general_label_cancel) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_template_chat, container, false)
        counter = rootView.findViewById(R.id.counter)
        error = rootView.findViewById(R.id.error)
        submit = rootView.findViewById(R.id.submit)
        editText = rootView.findViewById(R.id.edittext)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        arguments?.let {
            message = it.getString(InboxMessageConstant.PARAM_MESSAGE)
            list = it.getStringArrayList(InboxMessageConstant.PARAM_ALL)?: arrayListOf()
        }
        editText?.setText(message)
        message?.let {
            editText?.setSelection(it.length)
        }
        counterObservable = Events.text(editText).map { s -> s.length }
        val onNextAction = Action1 { integer: Int ->
            activity?.runOnUiThread {
                showErrorAndProceed(integer, submit)
                counter?.text = String.format("%d/%d", integer, MAX_CHAR)
            }
        }
        val onError = Action1 { throwable: Throwable -> throwable.printStackTrace() }
        counterObservable?.subscribe(onNextAction, onError)
        submit?.setOnClickListener {
            when (mode) {
                TemplateChatFragment.CREATE -> analytics.trackCreateSaveTemplateChat()
                TemplateChatFragment.EDIT -> analytics.trackEditSaveTemplateChat()
            }
            viewModel.submitText(editText?.text.toString(), message?: "", list, isSeller)
        }
    }

    private val mode: Int
        get() = arguments?.getInt(InboxMessageConstant.PARAM_MODE, -2) ?: -2

    private fun showErrorAndProceed(integer: Int, proceed: TextView?) {
        when {
            integer == 0 -> {
                canProceed(false, proceed)
            }
            integer < MIN_CHAR -> {
                error?.text = getString(R.string.minimal_char_template)
                error?.visibility = View.VISIBLE
                canProceed(false, proceed)
            }
            integer > MAX_CHAR -> {
                error?.text = getString(
                    R.string.maximal_char_template,
                    MAX_CHAR
                )
                error?.visibility = View.VISIBLE
                canProceed(false, proceed)
            }
            else -> {
                error?.visibility = View.GONE
                canProceed(true, proceed)
            }
        }
    }

    private fun canProceed(can: Boolean, proceed: TextView?) {
        proceed?.isEnabled = can
        if (can) {
            proceed?.background?.setColorFilter(
                MethodChecker.getColor(
                    activity, com.tokopedia.unifyprinciples.R.color.Unify_G400
                ), PorterDuff.Mode.SRC_IN
            )
            proceed?.setTextColor(
                MethodChecker.getColor(
                    activity, com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            )
        } else {
            proceed?.background?.setColorFilter(
                MethodChecker.getColor(
                    activity, com.tokopedia.unifyprinciples.R.color.Unify_N100
                ), PorterDuff.Mode.SRC_IN
            )
            proceed?.setTextColor(
                MethodChecker.getColor(
                    activity, com.tokopedia.unifyprinciples.R.color.Unify_N200
                )
            )
        }
    }

    override fun getScreenName(): String {
        return EditTemplateChatFragment::class.java.simpleName
    }

    override fun initInjector() {
        initializeComponent().inject(this)
    }

    private fun initializeComponent(): TemplateChatComponent {
        return ActivityComponentFactory.instance.createActivityComponent(requireActivity())
    }

    private fun onResult(editTemplateViewModel: EditTemplateResultModel, index: Int, s: String) {
        analytics.eventClickTemplate()
        val intent = Intent()
        intent.putExtra(TemplateChatFragment.INDEX_RESULT, index)
        intent.putExtra(TemplateChatFragment.LIST_RESULT, s)
        intent.putExtra(ENABLED_KEY_RESULT, editTemplateViewModel.isEnabled)
        intent.putExtra(
            TemplateChatFragment.MODE_RESULT,
            arguments?.getInt(InboxMessageConstant.PARAM_MODE)
        )
        activity?.setResult(Activity.RESULT_OK, intent)
    }

    private fun onResult(editTemplateViewModel: EditTemplateResultModel, index: Int) {
        val intent = Intent()
        intent.putExtra(TemplateChatFragment.INDEX_RESULT, index)
        intent.putExtra(TemplateChatFragment.MODE_RESULT, TemplateChatFragment.DELETE)
        activity?.setResult(Activity.RESULT_OK, intent)
    }

    private fun finish() {
        activity?.finish()
    }

    private fun dropKeyboard() {
        KeyboardHandler.DropKeyboard(activity, view)
    }

    private fun showError(error: Throwable) {
        SnackbarManager.make(
            activity, ErrorHandler.getErrorMessage(
                context, error
            ), Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setupObservers() {
        viewModel.createEditTemplate.observe(viewLifecycleOwner, {
            onResult(it.editTemplateResultModel, it.index, it.text)
            finish()
        })

        viewModel.deleteTemplate.observe(viewLifecycleOwner, {
            val editTemplateUiModel = it.first
            val index = it.second
            onResult(editTemplateUiModel, index)
            finish()
        })

        viewModel.errorAction.observe(viewLifecycleOwner, {
            showError(it)
        })
    }

    companion object {
        private const val EMPTY_CHAR = 0
        private const val MIN_CHAR = 5
        private const val MAX_CHAR = 200
        private const val DISABLE_DELETE = 130
        private const val ENABLE_DELETE = 255
        private const val ENABLED_KEY_RESULT = "enabled"

        fun createInstance(extras: Bundle?): EditTemplateChatFragment {
            val fragment = EditTemplateChatFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}