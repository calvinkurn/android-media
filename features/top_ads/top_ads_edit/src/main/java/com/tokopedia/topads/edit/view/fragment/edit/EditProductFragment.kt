package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.ADDED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.DELETED_PRODUCTS
import com.tokopedia.topads.edit.utils.Constants.EXISTING_IDS
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.REQUEST_OK
import com.tokopedia.topads.edit.utils.Constants.RESULT_IMAGE
import com.tokopedia.topads.edit.utils.Constants.RESULT_NAME
import com.tokopedia.topads.edit.utils.Constants.RESULT_PRICE
import com.tokopedia.topads.edit.utils.Constants.RESULT_PROUCT
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.activity.SelectProductActivity
import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapter
import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_edit_fragment_product_list_edit.*
import javax.inject.Inject

class EditProductFragment : BaseDaggerFragment() {

    private var buttonStateCallback: SaveButtonStateCallBack? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: EditProductListAdapter
    private var originalIdList: MutableList<Int> = arrayListOf()
    private var deletedProducts: MutableList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = mutableListOf()
    private var addedProducts: MutableList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = mutableListOf()
    private var btnState = true
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }
    private val sharedViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_fragment_product_list_edit), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EditProductListAdapter(EditProductListAdapterTypeFactoryImpl(this::onProductListDeleted))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val groupId = arguments?.getString(GROUP_ID)
        viewModel.getAds(groupId?.toInt(), this::onSuccessGetAds)
    }

    private fun onSuccessGetAds(data: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>) {
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
                list.add(it.data.itemID.toString())
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
        add_product.setOnClickListener {
            val intent = Intent(context, SelectProductActivity::class.java)
            intent.putIntegerArrayListExtra(EXISTING_IDS, adapter.getCurrentIds())
            startActivityForResult(intent, 1)
        }
        product_list.adapter = adapter
        product_list.layoutManager = LinearLayoutManager(context)
    }

    private fun onProductListDeleted(pos: Int) {
        deletedProducts.add((adapter.items[pos] as EditProductItemViewModel).data)
        adapter.items.removeAt(pos)
        adapter.notifyDataSetChanged()
        updateItemCount()
        if (adapter.items.isEmpty()) {
            adapter.items.add(EditProductEmptyViewModel())
            btnState = false
            buttonStateCallback?.setButtonState()
            adapter.notifyDataSetChanged()
        }
        sharedViewModel.setProductIds(getProductIds())

    }

    private fun updateItemCount() {
        product_count.text = String.format(getString(R.string.product_count), adapter.items.size)

    }

    private fun setVisibilityOperation(visiblity: Int) {
        btnState = visiblity == View.VISIBLE
        buttonStateCallback?.setButtonState()
            product_count.visibility = visiblity
            add_product.visibility = visiblity
            add_image.visibility = visiblity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OK) {
            if (resultCode == Activity.RESULT_OK) {
                createProduct(data?.getStringArrayListExtra(RESULT_PRICE), data?.getIntegerArrayListExtra(RESULT_PROUCT), data?.getStringArrayListExtra(RESULT_NAME), data?.getStringArrayListExtra(RESULT_IMAGE))
                sharedViewModel.setProductIds(getProductIds())

            }
        }
    }

    private fun createProduct(price: ArrayList<String>?, product: ArrayList<Int>?, name: ArrayList<String>?, image: ArrayList<String>?) {
        if (adapter.items.isNotEmpty() && adapter.items[0] is EditProductEmptyViewModel)
            adapter.items.clear()
        product?.forEachIndexed {ind,it ->
            val dataItem = GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem.AdDetailProduct(image?.get(ind)!!, image[ind], name?.get(ind)!!)
            adapter.items.add(EditProductItemViewModel(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(product[ind], price?.get(ind)!!, "", 0, 0, dataItem)))
            if (!existsOriginal(it)) {
                addedProducts.add(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(product[ind], price[ind], "", 0, 0, dataItem))
            }
        }
        adapter.notifyDataSetChanged()
        updateItemCount()
        btnState = true
        buttonStateCallback?.setButtonState()

    }

    private fun existsOriginal(id: Int): Boolean {
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
        return bundle
    }

    private fun filterAddedProducts() {
        /// for the products which are added and removed
        val iterator = addedProducts.iterator()
        while (iterator.hasNext()){
            val key = iterator.next()
            deletedProducts.forEach { deleted ->
                if (key.itemID == deleted.itemID) {
                    iterator.remove()
                }
            }
        }

        //remove the delete items which are not originally in the group
        var indi: MutableList<Int> = mutableListOf()
        deletedProducts.forEachIndexed { index, deleted ->
            if ((originalIdList.find { it -> deleted.itemID == it }) == null) {
                indi.add(index)
            }
        }
        for (index in indi) {
            deletedProducts.removeAt(index)
        }
    }

    private fun getAddedArray(): List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> {
        val list: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = arrayListOf()
        addedProducts.forEach {
            list.add(it)
        }
        return list
    }

    private fun getDeletedArray(): List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> {
        val list: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = arrayListOf()
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

