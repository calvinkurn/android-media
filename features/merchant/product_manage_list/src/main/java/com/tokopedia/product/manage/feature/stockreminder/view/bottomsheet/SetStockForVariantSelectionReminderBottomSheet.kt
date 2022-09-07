package com.tokopedia.product.manage.feature.stockreminder.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetSetAtOnceStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.EMPTY_INPUT_STOCK
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.MINIMUM_STOCK_REMINDER
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.MAXIMUM_STOCK_REMINDER
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_ACTIVE
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_INACTIVE
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SetStockForVariantSelectionReminderBottomSheet(
    private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(maxStock: Int?,
                           fm: FragmentManager? = null): SetStockForVariantSelectionReminderBottomSheet {
            return SetStockForVariantSelectionReminderBottomSheet(fm).apply {
                arguments = Bundle().apply {
                    maxStock?.let {
                        putInt(MAX_STOCK_KEY, it)
                    }
                }
            }
        }

        private const val MAX_STOCK_KEY = "max_stock"

        private val TAG: String = SetStockForVariantSelectionReminderBottomSheet::class.java.simpleName
    }

    private var maxStock: Int? = null
    private var textChangeListener: TextWatcher? = null

    private var binding by autoClearedNullable<BottomSheetSetAtOnceStockReminderBinding>()

    private var applyListener: (Int,Int) -> Unit =
        { _: Int, _: Int -> }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSetAtOnceStockReminderBinding.inflate(
            inflater,
            container,
            false
        )
        setupView()
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            maxStock = arguments?.getInt(MAX_STOCK_KEY, MAXIMUM_STOCK_REMINDER)
        } else {
            maxStock = savedInstanceState.getInt(MAX_STOCK_KEY)
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@SetStockForVariantSelectionReminderBottomSheet)?.commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        maxStock?.let {
            outState.putInt(MAX_STOCK_KEY, it)
        }
    }

    fun setOnApplyListener(applyListener: (stockReminder:Int,reminderStatus:Int) -> Unit) {
        this.applyListener = applyListener
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }

    private fun setupView() {
        val title =
            context?.getString(R.string.product_stock_reminder_set_at_once).orEmpty()
        setTitle(title)
        setupStockEditorText()
        setAddButtonClickListener()
        setSubtractButtonClickListener()
        setupStatusSwitch()
        binding?.buttonApply?.setOnClickListener {
            dismiss()
            applyData()
        }
        binding?.clRemainingStock?.setOnClickListener {
            showRemainingStockBottomSheet()
        }
    }

    private fun setupStatusSwitch() {
        binding?.swStockReminder?.setOnCheckedChangeListener { _, _ ->
            val stockLimit = binding?.qeStock?.getValue().orZero()
            validateMinMaxStock(stockLimit)
        }
    }

    private fun applyData() {
        val stock = binding?.qeStock?.getValue().orZero()
        val status = if (binding?.swStockReminder?.isChecked.orTrue()) {
            REMINDER_ACTIVE
        } else {
            REMINDER_INACTIVE
        }
        applyListener(stock,status)
    }

    private fun setupStockEditorText() {
        binding?.qeStock?.editText?.run {
            val maxLength = InputFilter.LengthFilter(StockReminderConst.MAXIMUM_LENGTH)
            filters = arrayOf(maxLength)
            textChangeListener = createTextChangeListener()
            addTextChangedListener(textChangeListener)
        }
        binding?.qeStock?.setValue(MINIMUM_STOCK_REMINDER)

        binding?.qeStock?.run {
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    KeyboardHandler.DropKeyboard(context, this)
                }
                true
            }
            maxValue = getMaxStock()
        }
    }

    private fun createTextChangeListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(editor: Editable?) {
                val input = editor.toString()
                val stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }
                validateMinMaxStock(stock)
                toggleQuantityEditorBtn(stock)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
    }

    private fun validateMinMaxStock(stock: Int) {
        when {
            stock < MINIMUM_STOCK_REMINDER -> {
                binding?.qeStock?.errorMessageText = activity?.getString(
                    R.string.product_stock_reminder_min_stock_error,
                    MINIMUM_STOCK_REMINDER
                ).orEmpty()
            }
            stock > getMaxStock() -> {
                binding?.qeStock?.errorMessageText = activity?.getString(
                    R.string.product_stock_reminder_max_stock_error,
                    getMaxStock().getNumberFormatted()
                ).orEmpty()
            }
            else -> {
                binding?.qeStock?.errorMessageText = String.EMPTY
            }
        }
        binding?.buttonApply?.isEnabled = !(stock < MINIMUM_STOCK_REMINDER && stock > getMaxStock())

    }

    private fun setAddButtonClickListener() {
        binding?.qeStock?.run {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }

                stock++

                if (stock <= binding?.qeStock?.maxValue.orZero()) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun setSubtractButtonClickListener() {
        binding?.qeStock?.run {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }

                stock--

                if (stock > EMPTY_INPUT_STOCK) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < getMaxStock()

        binding?.qeStock?.run {
            addButton.isEnabled = enableAddBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    private fun showRemainingStockBottomSheet() {
        val stockRemainingInfoBottomSheet = StockRemainingInfoBottomSheet(
            childFragmentManager
        )
        stockRemainingInfoBottomSheet.show()
    }

    private fun getMaxStock(): Int {
        return maxStock ?: MAXIMUM_STOCK_REMINDER
    }

}