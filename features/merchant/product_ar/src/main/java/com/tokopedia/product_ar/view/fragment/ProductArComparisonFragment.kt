package com.tokopedia.product_ar.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.di.ProductArComponent
import com.tokopedia.product_ar.util.ItemDividerGrid
import com.tokopedia.product_ar.view.adapter.PhotoComparisonAdapter
import com.tokopedia.product_ar.viewmodel.ProductArComparisonViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class ProductArComparisonFragment : BaseDaggerFragment(), asdInterface {

    companion object {
        const val PRODUCT_AR_COMPARISON_FRAGMENT = "product_ar_fragment"
        fun newInstance() = ProductArComparisonFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ProductArComparisonViewModel? = null
    private var rvComparison: RecyclerView? = null
    private var comparisonAdapter: PhotoComparisonAdapter = PhotoComparisonAdapter(this)
    private var btnTest: UnifyButton? = null
    private var rvHeight: Int = 0
    private var navToolbar: NavToolbar? = null
    private var layoutManager: GridLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_ar_comparison_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductArComparisonViewModel::class.java)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProductArComponent::class.java)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupNavToolbar()
        setupRv()
    }

    private fun setupRv() {
        val mIcon = BitmapFactory.decodeResource(requireContext().resources, R.drawable.asd)
        layoutManager = GridLayoutManager(context, 1)
        rvComparison?.layoutManager = layoutManager
        rvComparison?.itemAnimator = DefaultItemAnimator()
        rvComparison?.adapter = comparisonAdapter
        comparisonAdapter.setData(listOf(mIcon))

        rvComparison?.addOneTimeGlobalLayoutListener {
            val rvMargin = rvComparison?.layoutParams as? ViewGroup.MarginLayoutParams
            rvHeight = rvComparison?.height ?: 0 - (rvMargin?.bottomMargin ?: 0)
        }

        btnTest?.setOnClickListener {
            val a = mutableListOf<Bitmap>(mIcon)
            a.addAll(comparisonAdapter.listBitmap)

            if (a.size > 1) {
                layoutManager?.spanCount = 2
                if (rvComparison?.itemDecorationCount == 0) {
                    rvComparison?.addItemDecoration(ItemDividerGrid())
                }
            } else {
                layoutManager?.spanCount = 1
            }

            comparisonAdapter.setData(a)
        }
    }

    private fun initView(view: View) {
        rvComparison = view.findViewById(R.id.rv_ar_comparison)
        btnTest = view.findViewById(R.id.btn_atc_ar_comparison)
        navToolbar = view.findViewById(R.id.product_ar_comparison_toolbar)
    }

    override fun getRvHeight(): Int {
        return rvHeight
    }

    override fun onImageClick(position: Int) {
        if (comparisonAdapter.listBitmap.size - 1 == 1) {
            layoutManager?.spanCount = 1
            val asd = comparisonAdapter.listBitmap
            asd.removeAt(position)
            comparisonAdapter.setData(asd)
        } else {
            comparisonAdapter.removeData(position)
        }
    }

    private fun setupNavToolbar() {
        activity?.let { activity ->
            navToolbar?.run {
                setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                setToolbarTitle("")
                setupToolbarWithStatusBar(activity, NavToolbar.Companion.StatusBar.STATUS_BAR_LIGHT)
            }
        }
    }
}

interface asdInterface {
    fun getRvHeight(): Int
    fun onImageClick(position: Int)
}