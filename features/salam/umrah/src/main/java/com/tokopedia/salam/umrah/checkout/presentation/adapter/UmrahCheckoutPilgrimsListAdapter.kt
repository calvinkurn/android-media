package com.tokopedia.salam.umrah.checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder.UmrahPilgrimsEmptyViewHolder
import com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder.UmrahPilgrimsFilledViewHolder

/**
 * @author by firman on 19/11/19
 */

class UmrahCheckoutPilgrimsListAdapter(val listenerEmpty: UmrahPilgrimsEmptyViewHolder.UmrahCheckoutPilgrimsListListener,
                                       val listenerFilled: UmrahPilgrimsFilledViewHolder.UmrahCheckoutPilgrimsListListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var listUmrahCheckoutPilgrims = mutableListOf<UmrahCheckoutPilgrims>()


    override fun getItemCount(): Int = listUmrahCheckoutPilgrims.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            TYPE_EMPTY-> {
                val emptyHolder : UmrahPilgrimsEmptyViewHolder = holder as UmrahPilgrimsEmptyViewHolder
                emptyHolder.bind(listUmrahCheckoutPilgrims[position])
            }
            TYPE_FILLED ->{
                val filledHolder : UmrahPilgrimsFilledViewHolder = holder as UmrahPilgrimsFilledViewHolder
                filledHolder.bind(listUmrahCheckoutPilgrims[position])
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (listUmrahCheckoutPilgrims.get(position).firstName.isEmpty()) {
            TYPE_EMPTY
        } else {
            TYPE_FILLED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return when(viewType){
          TYPE_EMPTY ->  UmrahPilgrimsEmptyViewHolder(LayoutInflater.from(parent.context).
                  inflate(R.layout.item_umrah_checkout_pilgrims, parent, false),listenerEmpty)
          TYPE_FILLED ->  UmrahPilgrimsFilledViewHolder(LayoutInflater.from(parent.context).
                  inflate(R.layout.item_umrah_checkout_pilgrims_filled, parent, false),listenerFilled)
          else ->  UmrahPilgrimsEmptyViewHolder(LayoutInflater.from(parent.context).
                  inflate(R.layout.item_umrah_checkout_pilgrims, parent, false),listenerEmpty)
      }
    }

     fun onReplaceData(data: UmrahCheckoutPilgrims, index: Int){
         listUmrahCheckoutPilgrims.set(index, data)
         notifyItemChanged(index)
    }

    fun setList(list: MutableList<UmrahCheckoutPilgrims>) {
        listUmrahCheckoutPilgrims = list
        notifyDataSetChanged()
    }

    companion object {
        const val TYPE_EMPTY = 1
        const val TYPE_FILLED = 2
    }

}