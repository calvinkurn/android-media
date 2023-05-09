package com.tokopedia.shop.settings.etalase.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.settings.databinding.FragmentPowerMerchantAccessBinding
import com.tokopedia.shop.settings.etalase.data.PowerMerchantAccessModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PowerMerchantAccessBottomSheet : BottomSheetUnify() {

    companion object {
        private const val MODEL = "model"

        @JvmStatic
        fun newInstance(model: PowerMerchantAccessModel): PowerMerchantAccessBottomSheet {
            return PowerMerchantAccessBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(MODEL, model)
                arguments = bundle
            }
        }
    }

    private var binding by autoClearedNullable<FragmentPowerMerchantAccessBinding>()

    private var listener: BottomSheetListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPowerMerchantAccessBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setData() {
        val model = arguments?.getParcelable(MODEL) ?: PowerMerchantAccessModel()
        binding?.apply {
            imageViewIcon.loadImage(model.imageUrl)
            textViewTitle.text = MethodChecker.fromHtml(model.title)
            textViewDescription.text = MethodChecker.fromHtml(model.desc)
            buttonRedirectTo.text = model.btnTitle
            buttonRedirectTo.setOnClickListener {
                listener?.onBottomSheetButtonClicked()
            }
        }
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    interface BottomSheetListener {
        fun onBottomSheetButtonClicked()
    }
}
