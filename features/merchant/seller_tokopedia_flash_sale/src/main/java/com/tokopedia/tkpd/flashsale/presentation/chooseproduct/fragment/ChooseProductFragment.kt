package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.ChooseProductAdapter
import com.tokopedia.campaign.components.adapter.ChooseProductDelegateAdapter
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentChooseProductBinding
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChooseProductFragment : BaseSimpleListFragment<CompositeAdapter, ChooseProductItem>(),
    ChooseProductDelegateAdapter.ChooseProductListener {

    private var binding by autoClearedNullable<StfsFragmentChooseProductBinding>()
    private val chooseProductAdapter: ChooseProductAdapter = ChooseProductAdapter()

    override fun getScreenName(): String = ChooseProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentChooseProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onChooseProductClicked(index: Int, item: ChooseProductItem, selected: Boolean) {
        println(item)
    }

    override fun onDetailClicked(index: Int) {
        println("click $index")
    }

    override fun createAdapter(): CompositeAdapter = chooseProductAdapter.getRecyclerViewAdapter()

    override fun getRecyclerView(view: View): RecyclerView? = binding?.rvBundle

    override fun getPerPage(): Int = generate().size

    override fun addElementToAdapter(list: List<ChooseProductItem>) {
        chooseProductAdapter.addItems(list)
    }

    override fun loadData(page: Int, offset: Int) {
        view?.postDelayed( {
            renderList(generate(), page < 3)
        }, 2000)
    }

    override fun clearAdapterData() {
        chooseProductAdapter.submit(listOf())
    }

    override fun onShowLoading() {
        chooseProductAdapter.setLoading(true)
    }

    override fun onHideLoading() {
        chooseProductAdapter.setLoading(false)
    }

    override fun onDataEmpty() {
        //
    }

    override fun onGetListError(message: String) {
        //
    }

    private fun generate(): List<ChooseProductItem> {
        return listOf(
            ChooseProductItem(
                productId = "111",
                productName = "Name Example",
                imageUrl = "https://placekitten.com/1000/1000",
                variantText = "5 Varian Produk",
                variantTips = "3 varian sesuai kriteria",
                priceText = "Rp75.000.000-Rp120.000.000",
                stockText = "Total Stok: 999 di 5 lokasi",
                errorMessage = "Kuota pengajuan produkmu sudah habis.",
                isError = true,
                isEnabled = true,
                showCheckDetailCta = true,
                isSelected = true
            ),
            ChooseProductItem(
                productId = "112",
                productName = "Name Example2 duo",
                imageUrl = "https://placekitten.com/700/1000",
                variantText = "9 Varian Produk",
                variantTips = "1 varian sesuai kriteria",
                priceText = "Rp75.000.000",
                stockText = "Total Stok: 999 di 5 lokasi",
                errorMessage = "Kuota pengajuan produkmu sudah habis.",
                isError = true,
                isEnabled = false,
                showCheckDetailCta = true,
                isSelected = false
            ),
            ChooseProductItem(
                productId = "113",
                productName = "Name Example3",
                imageUrl = "https://placekitten.com/1000/1000",
                variantText = "5 Varian Produk",
                priceText = "Rp120.000.000",
                stockText = "Total Stok: 999 di 5 lokasi",
                isEnabled = true,
                isSelected = false
            ),
            ChooseProductItem(
                productId = "111",
                productName = "Name Example",
                imageUrl = "https://placekitten.com/1000/1000",
                variantText = "5 Varian Produk",
                variantTips = "3 varian sesuai kriteria",
                priceText = "Rp75.000.000-Rp120.000.000",
                stockText = "Total Stok: 999 di 5 lokasi",
                errorMessage = "Kuota pengajuan produkmu sudah habis.",
                isError = true,
                isEnabled = true,
                showCheckDetailCta = true,
                isSelected = true
            ),
            ChooseProductItem(
                productId = "112",
                productName = "Name Example2 duo",
                imageUrl = "https://placekitten.com/700/1000",
                variantText = "9 Varian Produk",
                variantTips = "1 varian sesuai kriteria",
                priceText = "Rp75.000.000",
                stockText = "Total Stok: 999 di 5 lokasi",
                errorMessage = "Kuota pengajuan produkmu sudah habis.",
                isError = true,
                isEnabled = false,
                showCheckDetailCta = true,
                isSelected = false
            ),
            ChooseProductItem(
                productId = "113",
                productName = "Name Example3",
                imageUrl = "https://placekitten.com/1000/1000",
                variantText = "5 Varian Produk",
                priceText = "Rp120.000.000",
                stockText = "Total Stok: 999 di 5 lokasi",
                isEnabled = true,
                isSelected = false
            ),
        )
    }
}