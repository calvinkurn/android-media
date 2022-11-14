package com.tokopedia.tokopedianow.similarproduct.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.configureMaxHeight
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowSimilarProductsBinding
import com.tokopedia.tokopedianow.similarproduct.adapter.SimilarProductAdapter
import com.tokopedia.tokopedianow.similarproduct.adapter.SimilarProductAdapterTypeFactory
import com.tokopedia.tokopedianow.similarproduct.analytic.ProductAnalytics
import com.tokopedia.tokopedianow.similarproduct.viewholder.SimilarProductViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowSimilarProductBottomSheet : BottomSheetUnify() {

    companion object {

        fun newInstance(): TokoNowSimilarProductBottomSheet {
            return TokoNowSimilarProductBottomSheet()
        }

        private val TAG: String = TokoNowSimilarProductBottomSheet::class.java.simpleName
    }

    var items: List<Visitable<*>> = emptyList()
        set(value) {
            field = value
            submitList(value)
        }
    var productListener: SimilarProductViewHolder.SimilarProductListener? = null
    var productAnalytics: ProductAnalytics? = null

    private var binding by autoClearedNullable<BottomsheetTokopedianowSimilarProductsBinding>()

    private val adapter by lazy {
        SimilarProductAdapter(
            SimilarProductAdapterTypeFactory(
                productListener = productListener,
                productAnalytics = productAnalytics
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetTokopedianowSimilarProductsBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        configureBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMaxHeight(true)
        setupRecyclerView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun showToaster(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickAction: View.OnClickListener = View.OnClickListener { }
    ) {
        Toaster.toasterCustomBottomHeight = context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_40
        ).toIntSafely()
        view?.rootView?.let {
            if (message.isNotBlank()) {
                val toaster = Toaster.build(
                    view = it,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickAction
                )
                toaster.show()
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun configureBottomSheet() {
        clearContentPadding = true
        showCloseIcon = false
        isDragable = true
        isHideable = true
        showKnob = true
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@TokoNowSimilarProductBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
        }
        submitList(items)
    }

    private fun submitList(value: List<Visitable<*>>) {
        adapter.submitList(value)
    }
}
