package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital_checkout.databinding.LayoutDigitalCheckoutMyBillsSectionBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 14/01/21
 */

class DigitalCartMyBillsWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = LayoutDigitalCheckoutMyBillsSectionBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    var actionListener: ActionListener? = null
        set(value) {
            field = value
            field?.let { listener ->
                binding.icCheckoutMyBillsInfo.setOnClickListener { listener.onMoreInfoClicked() }
                binding.checkBoxCheckoutMyBills.setOnCheckedChangeListener { _, isChecked ->
                    listener.onCheckChanged(isChecked)
                }
            }
        }

    fun setTitle(title: String) {
        binding.tvCheckoutMyBillsHeaderTitle.text = MethodChecker.fromHtml(title)
    }

    fun setDescription(description: String) {
        binding.tvCheckoutMyBillsDescription.shouldShowWithAction(description.isNotEmpty()){
            binding.tvCheckoutMyBillsDescription.text = MethodChecker.fromHtml(description)
        }
    }

    fun setChecked(isChecked: Boolean) {
        binding.checkBoxCheckoutMyBills.isChecked = isChecked
    }

    fun isChecked(): Boolean = binding.checkBoxCheckoutMyBills.isChecked

    fun disableCheckBox() {
        binding.checkBoxCheckoutMyBills.hide()
    }

    fun hasMoreInfo(state: Boolean) {
        binding.icCheckoutMyBillsInfo.showWithCondition(state)
    }

    fun setAdditionalImage(imageUrl: String) {
        with(binding){
            if (imageUrl.isNotEmpty()) {
                ivAdditionalFintechImage.loadImage(imageUrl)
                ivAdditionalFintechImage.show()
                bgAdditionalFintechImage.show()
            } else {
                ivAdditionalFintechImage.hide()
                bgAdditionalFintechImage.hide()
            }
        }
    }

    interface ActionListener {
        fun onMoreInfoClicked()
        fun onCheckChanged(isChecked: Boolean)
    }

}