package com.tokopedia.content.common.producttag.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.BottomSheetProductTagSourceBinding
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagSourceBottomSheet @Inject constructor(
    private val userSession: UserSessionInterface,
) : BottomSheetUnify() {

    private var _binding: BottomSheetProductTagSourceBinding? = null
    private val binding: BottomSheetProductTagSourceBinding
        get() = _binding!!

    private var mAnalytic: ContentProductTagAnalytic? = null

    private var mListener: Listener? = null
    private var mProductTagSourceList = mutableListOf<ProductTagSource>()
    private var mShopBadge: String = ""
    private var mAuthorId: String = ""
    private var mAuthorType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        mAnalytic?.viewProductTagSourceBottomSheet(mAuthorId, mAuthorType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetProductTagSourceBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)

        setCloseClickListener {
            mAnalytic?.clickCloseOnProductTagSourceBottomSheet(mAuthorId, mAuthorType)
            dismiss()
        }
    }

    private fun setupView() {
        setTitle(getString(R.string.cc_product_tag_source_label))
        setShopInfo()
        updateList()

        binding.clGlobalSearch.setOnClickListener {
            mListener?.onSelectProductTagSource(ProductTagSource.GlobalSearch)
            dismiss()
        }

        binding.clLastPurchase.setOnClickListener {
            mListener?.onSelectProductTagSource(ProductTagSource.LastPurchase)
            dismiss()
        }

        binding.clMyShop.setOnClickListener {
            mListener?.onSelectProductTagSource(ProductTagSource.MyShop)
            dismiss()
        }
    }

    private fun setShopInfo() {
        if(userSession.hasShop()) {
            binding.icMyShop.showWithCondition(mShopBadge.isNotEmpty())
            binding.icMyShop.setImageUrl(mShopBadge)
            binding.tvMyShop.text = userSession.shopName
        }
    }

    private fun updateList() {
        val hasGlobalSearch = mProductTagSourceList.find { it == ProductTagSource.GlobalSearch } != null
        val hasLastPurchase = mProductTagSourceList.find { it == ProductTagSource.LastPurchase } != null
        val hasMyShop = mProductTagSourceList.find { it == ProductTagSource.MyShop } != null

        binding.clGlobalSearch.showWithCondition(hasGlobalSearch)
        binding.clLastPurchase.showWithCondition(hasLastPurchase)
        binding.clMyShop.showWithCondition(hasMyShop)

        binding.tvMyAccountLabel.showWithCondition(hasLastPurchase)
        binding.tvMyShopLabel.showWithCondition(hasMyShop)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(
        productTagSourceList: List<ProductTagSource>,
        shopBadge: String,
        authorId: String,
        authorType: String,
    ) {
        mProductTagSourceList.clear()
        mProductTagSourceList.addAll(productTagSourceList)

        mShopBadge = shopBadge

        mAuthorId = authorId
        mAuthorType = authorType
    }

    fun setAnalytic(analytic: ContentProductTagAnalytic?) {
        mAnalytic = analytic
    }

    fun showNow(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "FeedAccountTypeBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ProductTagSourceBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductTagSourceBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductTagSourceBottomSheet::class.java.name
            ) as ProductTagSourceBottomSheet
        }
    }

    interface Listener {
        fun onSelectProductTagSource(source: ProductTagSource)
    }
}
