package com.tokopedia.shop.settings.basicinfo.view.viewholder

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditMainInfoModel
import kotlinx.android.synthetic.main.item_shop_edit_main_info.view.*

class ShopEditMainInfoViewHolder (
        view: View,
        private val listener: ShopEditMainInfoListener?
): AbstractViewHolder<ShopEditMainInfoModel>(view) {
    companion object {
        private const val MIN_INPUT_LENGTH = 3
        @LayoutRes
        val LAYOUT = R.layout.item_shop_edit_main_info
        val POSITION = 2
    }

    private var shopDomainTextWatcher: TextWatcher? = null

    override fun bind(model: ShopEditMainInfoModel) {
        setup(model)
    }

    private fun setup(model: ShopEditMainInfoModel) {
        setShopDomainTextWatcher()
        setupShopNameTextField(model.name)
        setupShopDomainTextField(model.domain)
        setupShopTagLineTextField()
        setupShopDescriptionTextField()
        setupDomainSuggestion()
        setupTagLineDescription(model.tagLine, model.description)
        setupShopDomain(model.shopDomains)
        setupAllowed(model.isNameAllowed, model.isDomainAllowed)
        setupValidation(model)
    }

    private fun setShopDomainTextWatcher() {
        shopDomainTextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemView.shopDomainSuggestions.hide()
            }

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                if (input.length < MIN_INPUT_LENGTH) {
                    val message = itemView.context?.getString(R.string.shop_edit_domain_too_short).orEmpty()
                    showShopDomainInputError(message)
                    listener?.onCancelValidateShopDomain()
                } else {
                    resetShopDomainInput()
                    listener?.onValidateShopDomain(input)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        }
    }

    private fun setupShopNameTextField(name: String) {
        itemView.shopNameTextField.textFieldInput.apply {
            val textWatcher = createShopNameTextWatcher()
            setText(name)
            addTextChangedListener(textWatcher)
            isEnabled = false
        }
    }

    private fun setupShopDomainTextField(domain: String) {
        itemView.shopDomainTextField.textFieldInput.apply {
            setText(domain)
            addTextChangedListener(shopDomainTextWatcher)
            isEnabled = false
        }
    }

    private fun setupShopTagLineTextField() {
        itemView.shopTagLineTextField.textFieldInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                itemView.shopTagLineTextField.setMessage("")
                itemView.shopTagLineTextField.setError(false)
            }
        })
    }

    private fun setupShopDescriptionTextField() {
        itemView.shopDescriptionTextField.textFieldInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                itemView.shopDescriptionTextField.setMessage("")
                itemView.shopDescriptionTextField.setError(false)
            }
        })
    }

    private fun setupDomainSuggestion() {
        itemView.shopDomainSuggestions.setOnItemClickListener { domain ->
            itemView.shopDomainTextField.textFieldInput.apply {
                removeTextChangedListener(shopDomainTextWatcher)
                resetShopDomainInput()
                setText(domain)
                setSelection(text.length)
                addTextChangedListener(shopDomainTextWatcher)
            }
            itemView.shopDomainSuggestions.hide()
        }
    }

    private fun createShopNameTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemView.shopDomainSuggestions.hide()
            }

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                if (input.length < MIN_INPUT_LENGTH) {
                    val message = itemView.context?.getString(R.string.shop_edit_name_too_short).orEmpty()
                    showShopNameInputError(message)
                    listener?.onCancelValidateShopName()
                } else {
                    resetShopNameInput()
                    listener?.onValidateShopName(input)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        }
    }

    private fun setupTagLineDescription(tagLine: String, description: String) {
        itemView.shopTagLineTextField.textFieldInput.run {
            if (TextUtils.isEmpty(text)) {
                setText(tagLine)
                text?.length?.let { setSelection(it) }
            }
        }

        itemView.shopDescriptionTextField.textFieldInput.run {
            if (TextUtils.isEmpty(text)) {
                setText(description)
                text?.length?.let { setSelection(it) }
            }
        }
    }

    private fun resetShopNameInput() {
        itemView.shopNameTextField.setError(false)
        itemView.shopNameTextField.setMessage("")
    }

    private fun resetShopDomainInput() {
        itemView.shopDomainTextField.setError(false)
        itemView.shopDomainTextField.setMessage("")
    }

    private fun showShopNameInputError(message: String) {
        itemView.shopNameTextField.setError(true)
        itemView.shopNameTextField.setMessage(message)
    }

    private fun showShopDomainInputError(message: String) {
        itemView.shopDomainTextField.setError(true)
        itemView.shopDomainTextField.setMessage(message)
    }

    private fun setupShopDomain(shopDomains: List<String>) {
        itemView.shopDomainSuggestions.show(shopDomains)
    }

    private fun setupAllowed(isNameAllowed: Boolean, isDomainAllowed: Boolean) {
        val shopNameInput = itemView.shopNameTextField.textFieldInput
        val shopDomainInput = itemView.shopDomainTextField.textFieldInput

        shopNameInput.isEnabled = isNameAllowed
        shopDomainInput.isEnabled = isDomainAllowed
    }

    private fun setupValidation(model: ShopEditMainInfoModel) {
        validateShopName(model)
        validateShopDomain(model)
    }

    private fun validateShopName(model: ShopEditMainInfoModel) {
        if (model.nameNotValid) {
            showShopNameInputError(model.nameErrorMessage)
        } else if (model.nameErrorMessage.isNotBlank()) {
            showShopNameInputError(model.nameErrorMessage)
            itemView.shopDomainSuggestions.hide()
        }
    }

    private fun validateShopDomain(model: ShopEditMainInfoModel) {
        if (model.domainNotValid) {
            showShopDomainInputError(model.domainErrorMessage)
        } else if (model.domainErrorMessage.isNotBlank()) {
            showShopDomainInputError(model.domainErrorMessage)
            itemView.shopDomainSuggestions.hide()
        }
    }

    interface ShopEditMainInfoListener {
        fun onCancelValidateShopName()
        fun onCancelValidateShopDomain()
        fun onValidateShopName(name: String)
        fun onValidateShopDomain(domain: String)
    }
}