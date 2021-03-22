package com.tokopedia.shop.settings.notes.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.notes.data.ShopNoteBuyerViewUiModel
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteBuyerViewAdapter
import com.tokopedia.shop.settings.notes.view.viewmodel.ShopSettingsNoteBuyerViewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopSettingsNoteBuyerViewFragment : BaseDaggerFragment() {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"

        @JvmStatic
        fun createInstance(shopId: String?) = ShopSettingsNoteBuyerViewFragment().apply {
            arguments?.putString(SHOP_ID, shopId)
        }
    }

    @Inject
    lateinit var viewModel: ShopSettingsNoteBuyerViewViewModel

    private var buyerShopId: String? = null
    private var rvNote: RecyclerView? = null
    private var adapter: ShopNoteBuyerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buyerShopId = arguments?.getString(SHOP_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObserver()

        buyerShopId?.run {
            viewModel.getShopNotes(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_settings_note_buyer_view, container, false)
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ShopSettingsComponent::class.java).inject(this)
    }

    private fun setupUi() {
        context?.let{ context ->
            activity?.window?.decorView?.setBackgroundColor(
                    getColor(
                           context, com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
            )
        }

        rvNote = view?.findViewById(R.id.rv_note)
        adapter = ShopNoteBuyerViewAdapter()
        rvNote?.adapter = adapter
        rvNote?.layoutManager = LinearLayoutManager(context)
    }

    private fun setupObserver() {
        viewModel.shopNotes.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    adapter?.setItemsAndAnimateChanges(mapToShopNoteUiModel(result.data))
                }
                is Fail -> {
                    Log.d("ERROR-TEST", result.throwable.message)
                }
            }
        }
    }

    private fun mapToShopNoteUiModel(response: List<ShopNoteModel>): List<ShopNoteBuyerViewUiModel> {
        val notes = mutableListOf<ShopNoteBuyerViewUiModel>()
        response.forEach { model ->
            notes.add(
                    ShopNoteBuyerViewUiModel(
                        title = model.title ?: "",
                        description = model.content ?: ""
                    )
            )
        }
        return notes
    }

}