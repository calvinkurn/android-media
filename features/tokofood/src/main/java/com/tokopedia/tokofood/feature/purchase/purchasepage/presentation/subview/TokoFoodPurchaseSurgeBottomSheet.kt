package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseSurgeBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoFoodPurchaseSurgeBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(title: String,
                           desc: String): TokoFoodPurchaseSurgeBottomSheet {
            return TokoFoodPurchaseSurgeBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PARAM_TITLE, title)
                    putString(PARAM_DESC, desc)
                }
                isDragable = true
                isHideable = true
                showKnob = true
                showCloseIcon = false
            }
        }

        private const val PARAM_TITLE = "title"
        private const val PARAM_DESC = "desc"

        private const val TAG = "TokoFoodPurchaseSurgeBottomSheet"
    }

    private val title by lazy {
        arguments?.getString(PARAM_TITLE).orEmpty()
    }

    private val description by lazy {
        arguments?.getString(PARAM_DESC).orEmpty()
    }

    private var binding by autoClearedNullable<LayoutBottomSheetPurchaseSurgeBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(title)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutBottomSheetPurchaseSurgeBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.tvTokofoodSurgeDesc?.text = description
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

}