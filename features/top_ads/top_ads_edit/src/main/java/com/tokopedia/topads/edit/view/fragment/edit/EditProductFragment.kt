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
import com.tokopedia.topads.edit.view.activity.EditFormAdActivityCallback
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

    var callback: EditFormAdActivityCallback? = null
    private var buttonStateCallback: SaveButtonStateCallBack? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: EditFormDefaultViewModel

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: EditProductListAdapter
    private var originalIdList: MutableList<Int> = arrayListOf()
    private var deletedProducts: MutableList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = mutableListOf()
    private var addedProducts: MutableList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = mutableListOf()
    private var btnState = true


    companion object {

        private const val RESULT_PRICE = "resultPrice"
        private const val RESULT_PROUCT = "resultProduct"
        private const val RESULT_NAME = "resultName"
        private const val RESULT_IMAGE = "resultImage"
        private const val EXISTING_IDS = "ExistingIds"
        private const val ADDED_PRODUCTS = "addedProducts"
        private const val DELETED_PRODUCTS = "deletedProducts"
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
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_fragment_product_list_edit), container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditFormDefaultViewModel::class.java)
        adapter = EditProductListAdapter(EditProductListAdapterTypeFactoryImpl(this::onProductListDeleted))

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val groupId = arguments?.getString("groupId")
        viewModel.getAds(groupId?.toInt(), this::onSuccessGetAds, this::onErrorGetAds, this::onEmptyAds)
    }

    private fun onSuccessGetAds(data: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>) {
        data.forEach { result ->
            adapter.items.add(EditProductItemViewModel(result))
            sharedViewModel.setProductIds(getProductIds())
            originalIdList.add(result.itemID)
        }

        adapter.notifyDataSetChanged()
        setVisibilityOperation(true)
        updateItemCount()
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

    private fun onErrorGetAds(e: Throwable) {

    }

    private fun onEmptyAds() {
        adapter.items.add(EditProductEmptyViewModel())
        setVisibilityOperation(false)
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
        if (adapter.items.size == 0) {
            adapter.items.add(EditProductEmptyViewModel())
            adapter.notifyDataSetChanged()
        }

    }

    private fun updateItemCount() {
        product_count.text = String.format(getString(R.string.product_count), adapter.items.size)

    }


    private fun setVisibilityOperation(flag: Boolean) {
        btnState = flag
        buttonStateCallback?.setButtonState()
        if (flag) {
            product_count.visibility = View.VISIBLE
            add_product.visibility = View.VISIBLE
            add_image.visibility = View.VISIBLE
        } else {
            product_count.visibility = View.GONE
            add_product.visibility = View.GONE
            add_image.visibility = View.GONE

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                createProduct(data?.getStringArrayListExtra(RESULT_PRICE), data?.getIntegerArrayListExtra(RESULT_PROUCT), data?.getStringArrayListExtra(RESULT_NAME), data?.getStringArrayListExtra(RESULT_IMAGE))

            }
        }
    }

    private fun createProduct(price: ArrayList<String>?, product: ArrayList<Int>?, name: ArrayList<String>?, image: ArrayList<String>?) {
        val size = product?.size
        if (adapter.items[0] is EditProductEmptyViewModel)
            adapter.items.clear()
        for (ind in 0 until size!!) {
            val dataItem = GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem.AdDetailProduct(image?.get(ind)!!, image[ind], name?.get(ind)!!)
            adapter.items.add(EditProductItemViewModel(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(product[ind], price?.get(ind)!!, "", 0, 0, dataItem)))
            if (!existsOriginal(product[ind])) {
                addedProducts.add(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(product[ind], price[ind], "", 0, 0, dataItem))

            }
        }
        adapter.notifyDataSetChanged()
        sharedViewModel.setProductIds(getProductIds())
        updateItemCount()

    }

    private fun existsOriginal(id: Int): Boolean {
        return originalIdList.find { it -> id == it } != null

    }

    fun getButtonState(): Boolean {
        return btnState
    }

    override fun onAttach(context: Context) {
        if (context is EditFormAdActivityCallback) {
            callback = context
        }
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
        addedProducts.forEachIndexed { index, added ->
            deletedProducts.forEach { deleted ->
                if (added.itemID == deleted.itemID) {
                    addedProducts.removeAt(index)
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
        callback = null
        buttonStateCallback = null
        super.onDetach()
    }
}

