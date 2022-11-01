package com.tokopedia.loginregister.common.view.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.loginregister.databinding.ItemSocmedBottomsheetBinding
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.media.loader.loadImage

class SocmedBottomSheetAdapter constructor(
    private val providers: MutableList<ProviderData>,
    private val listener: SocmedBottomSheetListener
) : RecyclerView.Adapter<SocmedBottomSheetAdapter.ViewHolder>(){

    inner class ViewHolder(
        val viewBinding: ItemSocmedBottomsheetBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(providerData: ProviderData) {
            with(viewBinding) {
                providerName.text = providerData.name
                providerImage.loadImage(providerData.image) {
                    setPlaceHolder(-1)
                }

                root.setOnClickListener {
                    listener.onItemClick(providerData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemSocmedBottomsheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(providers[position])
    }

    override fun getItemCount(): Int {
        return providers.count()
    }

    companion object {
        fun instance(listener: SocmedBottomSheetListener): SocmedBottomSheetAdapter {
            return SocmedBottomSheetAdapter(mutableListOf(), listener)
        }
    }

}
