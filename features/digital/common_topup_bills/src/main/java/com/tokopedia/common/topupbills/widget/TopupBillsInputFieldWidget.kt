package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.view_voucher_game_input_field.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 20/08/19.
 */
open class TopupBillsInputFieldWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr), TopupBillsInputDropdownBottomSheet.OnClickListener {

    private var listener: ActionListener? = null

    private var isDropdown = false
    private var dropdownBottomSheet = BottomSheetUnify()
    private var dropdownView: TopupBillsInputDropdownBottomSheet? = null
    private var fragmentManager: FragmentManager? = null

    init {
        View.inflate(context, getLayout(), this)

        ac_input.clearFocus()

        btn_clear_input.setOnClickListener {
            ac_input.setText("")
            listener?.onFinishInput("")
            error_label.visibility = View.GONE
        }

        ac_input.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0 || isDropdown) {
                    btn_clear_input.visibility = View.GONE
                } else {
                    if (count > 1) {
                        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(ac_input, InputMethod.SHOW_FORCED)
                    }
                    btn_clear_input.visibility = View.VISIBLE
                }
            }

        })

        ac_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listener?.onFinishInput("")
                clearFocus()
            }
            false
        }
        ac_input.setKeyImeChangeListener { _, event ->
            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onFinishInput("")
                clearFocus()
            }
        }

        iv_input_dropdown.gone()

        dropdownBottomSheet.setFullPage(true)
        dropdownBottomSheet.clearAction()
        dropdownBottomSheet.setCloseClickListener {
            dropdownBottomSheet.dismiss()
        }

        ac_input.setOnFocusChangeListener { _, b ->
            onFocusChangeDropdown(b)
        }
    }

    fun setLabel(label: String) {
        input_label.text = label
    }

    fun setHint(hint: String) {
        ac_input.hint = hint
    }

    fun getInputText(): String {
        return ac_input.text.toString()
    }

    fun setInputText(input: String) {
        ac_input.setText(input)
    }

    fun setErrorMessage(message: String) {
        error_label.text = message
        error_label.visibility = View.VISIBLE
    }

    fun hideErrorMessage() {
        error_label.text = ""
        error_label.visibility = View.GONE
    }

    open fun getLayout(): Int {
        return R.layout.view_voucher_game_input_field
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    override fun onItemClicked(item: TopupBillsInputDropdownData) {
        ac_input.setText(item.value)
        dropdownBottomSheet.dismiss()
    }

    fun setupDropdownBottomSheet(data: List<TopupBillsInputDropdownData>) {
        isDropdown = true
        iv_input_dropdown.visible()

        dropdownView = TopupBillsInputDropdownBottomSheet(context, listener = this)
        dropdownView?.setData(data)

        this.fragmentManager = (context as AppCompatActivity).supportFragmentManager
        dropdownBottomSheet.setChild(dropdownView)
    }

    private fun showDropdownBottomSheet() {
        if (isDropdown && fragmentManager != null) {
            dropdownBottomSheet.show(fragmentManager,"Enquiry input field dropdown bottom sheet")
            // Open keyboard with delay so it opens when bottom sheet is fully visible
            Handler().postDelayed({
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }, SHOW_KEYBOARD_DELAY)
        }
    }

    private fun onFocusChangeDropdown(hasFocus: Boolean) {
        if (hasFocus && isDropdown) {
            ac_input.clearFocus()
            showDropdownBottomSheet()
        }
    }

    interface ActionListener {
        fun onFinishInput(input: String)
    }

    companion object {
        const val SHOW_KEYBOARD_DELAY: Long = 200
    }
}
