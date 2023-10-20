package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.header.HeaderUnify
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.ADDED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.ADDED_PRODUCTS_NEW
import com.tokopedia.topads.edit.utils.Constants.DELETED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.DELETED_PRODUCTS_NEW
import com.tokopedia.topads.edit.utils.Constants.EXISTING_IDS
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.REQUEST_OK
import com.tokopedia.topads.edit.utils.Constants.RESULT_IMAGE
import com.tokopedia.topads.edit.utils.Constants.RESULT_NAME
import com.tokopedia.topads.edit.utils.Constants.RESULT_PRICE
import com.tokopedia.topads.edit.utils.Constants.RESULT_PROUCT
import com.tokopedia.topads.edit.view.activity.EditAdGroupActivity
import com.tokopedia.topads.edit.view.activity.OnProductAction
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.activity.SelectProductActivity
import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR

private const val CLICK_TAMBAH_PRODUK = "click - tambah produk"
private const val PRODUCT_EDIT_NAME = "android.topads_edit"

class EditProductFragment : BaseDaggerFragment() {

    private var productCount: Typography? = null
    private var addImage: ImageUnify? = null
    private var addProduct: Typography? = null
    private var ctaDeleteProduct: UnifyButton? = null
    private var headerUnify: HeaderUnify? = null

    private var buttonStateCallback: SaveButtonStateCallBack? = null
    private var onProductAction: OnProductAction? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: EditProductListAdapter
    private var originalIdList: MutableList<String> = arrayListOf()
    private var deletedProducts: MutableList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> =
        mutableListOf()
    private var addedProducts: MutableList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> =
        mutableListOf()
    private var btnState = true
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(EditFormDefaultViewModel::class.java)
    }
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SharedViewModel::class.java)
    }

    companion object {

        fun newInstance(bundle: Bundle?): EditProductFragment {
            val fragment = EditProductFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return EditGroupAdFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view =
            inflater.inflate(resources.getLayout(R.layout.topads_edit_fragment_product_list_edit),
                container, false)
        recyclerView = view.findViewById(R.id.product_list)
        productCount = view.findViewById(R.id.product_count)
        addImage = view.findViewById(R.id.add_image)
        addProduct = view.findViewById(R.id.add_product)
        ctaDeleteProduct = view.findViewById(R.id.cta_delete_product)
        headerUnify = view.findViewById(R.id.header)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (currentPageNum < totalPage) {
                    currentPageNum++
                    fetchNextPage(currentPageNum)
                }
            }
        }
    }

    private fun fetchNextPage(page: Int) {
        viewModel.getAds(page,
            arguments?.getString(GROUP_ID),
            PRODUCT_EDIT_NAME,
            this::onSuccessGetAds)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter =
            EditProductListAdapter(EditProductListAdapterTypeFactoryImpl(this::onProductListDeleted))
        fetchData()
    }

    private fun fetchData() {
        viewModel.getAds(currentPageNum,
            arguments?.getString(GROUP_ID),
            PRODUCT_EDIT_NAME,
            this::onSuccessGetAds)
    }

    private fun onSuccessGetAds(
        data: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>,
        total: Int,
        perPage: Int,
    ) {
        totalCount = total
        totalPage = if (totalCount % perPage == 0) {
            totalCount / perPage
        } else
            (totalCount / perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        if (data.isEmpty()) {
            onEmptyAds()
        } else {
            data.forEach { result ->
                adapter.items.add(EditProductItemViewModel(result))
                originalIdList.add(result.itemID)
            }
            adapter.notifyDataSetChanged()
            setVisibilityOperation(View.VISIBLE)
            updateItemCount()
            sharedViewModel.setProductIds(getProductIds())
        }
    }

    private fun getProductIds(): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        adapter.items.forEach {
            if (it is EditProductItemViewModel) {
                list.add(it.data.itemID)
            }
        }
        return list
    }

    private fun onEmptyAds() {
        adapter.items.add(EditProductEmptyViewModel())
        setVisibilityOperation(View.GONE)
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addImage?.setImageDrawable(AppCompatResources.getDrawable(view.context, topadscommonR.drawable.topads_plus_add_keyword))
        addProduct?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_TAMBAH_PRODUK, "")
            val intent = Intent(context, SelectProductActivity::class.java)
            intent.putStringArrayListExtra(EXISTING_IDS, adapter.getCurrentIds())
            startActivityForResult(intent, 1)
        }
        ctaDeleteProduct?.setOnClickListener {
            onProductAction = activity as? EditAdGroupActivity
            onProductAction?.onAction(sendData())
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
        headerUnify?.setNavigationOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack() }
    }

    private fun onProductListDeleted(pos: Int) {
        deletedProducts.add((adapter.items[pos] as EditProductItemViewModel).data)
        adapter.items.removeAt(pos)
        adapter.notifyDataSetChanged()
        totalCount -= 1
        updateItemCount()
        if (adapter.items.isEmpty()) {
            adapter.items.add(EditProductEmptyViewModel())
            btnState = false
            buttonStateCallback?.setButtonState()
            adapter.notifyDataSetChanged()
        }

    }

    private fun updateItemCount() {
        productCount?.text = String.format(getString(topadscommonR.string.product_count), totalCount)
    }

    private fun setVisibilityOperation(visiblity: Int) {
        btnState = visiblity == View.VISIBLE
        buttonStateCallback?.setButtonState()
        productCount?.visibility = visiblity
        addProduct?.visibility = visiblity
        addImage?.visibility = visiblity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OK) {
            if (resultCode == Activity.RESULT_OK) {
                createProduct(data?.getStringArrayListExtra(RESULT_PRICE),
                    data?.getStringArrayListExtra(RESULT_PROUCT),
                    data?.getStringArrayListExtra(RESULT_NAME),
                    data?.getStringArrayListExtra(RESULT_IMAGE))
            }
        }
    }

    private fun createProduct(price: ArrayList<String>?, product: ArrayList<String>?, name: ArrayList<String>?, image: ArrayList<String>?) {
        if (adapter.items.isNotEmpty() && adapter.items[0] is EditProductEmptyViewModel)
            adapter.items.clear()
        product?.forEachIndexed { ind, it ->
            val dataItem = GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem.AdDetailProduct(image?.get(ind)
                    ?: "", image?.get(ind) ?: "", name?.get(ind) ?: "")
            adapter.items.add(EditProductItemViewModel(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(product[ind], price?.get(ind)!!, "", "0", "0", dataItem)))
            if (!existsOriginal(it)) {
                addedProducts.add(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(product[ind], price[ind], "", "0", "0", dataItem))
            }
        }
        adapter.notifyDataSetChanged()
        totalCount += product?.size ?: 0
        updateItemCount()
        Toaster.build(requireView(), String.format(getString(R.string.top_ads_add_product_toast), product?.count()), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
        btnState = true
        buttonStateCallback?.setButtonState()
    }

    private fun existsOriginal(id: String): Boolean {
        return originalIdList.find { id == it } != null
    }

    fun getButtonState(): Boolean {
        return btnState
    }

    override fun onAttach(context: Context) {
        if (context is SaveButtonStateCallBack) {
            buttonStateCallback = context
        }
        super.onAttach(context)
    }

    fun sendData(): Bundle {
        val bundle = Bundle()
        filterAddedProducts()
        val added: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = arrayListOf()
        val deleted: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = arrayListOf()
        added.addAll(getAddedArray())
        deleted.addAll(getDeletedArray())
        bundle.putParcelableArrayList(ADDED_PRODUCTS, added)
        bundle.putParcelableArrayList(DELETED_PRODUCTS, deleted)
        bundle.putParcelableArrayList(ADDED_PRODUCTS_NEW, added)
        bundle.putParcelableArrayList(DELETED_PRODUCTS_NEW, deleted)
        return bundle
    }

    private fun filterAddedProducts() {
        /// for the products which are added and removed
        deletedProducts.forEach { deleted ->
            val iterator = addedProducts.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                if (key.itemID == deleted.itemID) {
                    iterator.remove()
                    break
                }
            }
        }

        //remove the delete items which are not originally in the group
        val iteratorDel = deletedProducts.iterator()
        while (iteratorDel.hasNext()) {
            val key = iteratorDel.next()
            if (!originalIdList.contains(key.itemID)) {
                iteratorDel.remove()
            }
        }
    }

    private fun getAddedArray(): List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> {
        val list: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> =
            arrayListOf()
        addedProducts.forEach {
            list.add(it)
        }
        return list
    }

    private fun getDeletedArray(): List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> {
        val list: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> =
            arrayListOf()
        deletedProducts.forEach {
            list.add(it)
        }
        return list
    }

    override fun onDetach() {
        buttonStateCallback = null
        super.onDetach()
    }
}

