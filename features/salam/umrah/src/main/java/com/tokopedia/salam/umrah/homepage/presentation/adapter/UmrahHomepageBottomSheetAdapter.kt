package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBottomSheetData
import com.tokopedia.salam.umrah.homepage.data.UmrohHomepageBottomSheetwithType
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_umrah_home_page_bottom_sheet.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author by firman on 15/10/19
 */

class UmrahHomepageBottomSheetAdapter(context: Context,
                                      val listener: UmrahBottomSheetListener):
        RecyclerView.Adapter<UmrahHomepageBottomSheetAdapter.UmrahHomepageBottomSheetViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listBottomSheet = emptyList<UmrohHomepageBottomSheetwithType>()
    var lastCheckedPosition = 0
    lateinit var bottomSheetDialog : CloseableBottomSheetDialog

    inner class UmrahHomepageBottomSheetViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(data:UmrohHomepageBottomSheetwithType){
            with(itemView) {
                radio_umrah_home_page_bottom_sheet.isChecked = lastCheckedPosition == adapterPosition

                tv_umrah_home_page_bottom_sheet.text = data.displayText
                rl_umrah_home_page_bottom_sheet.setOnClickListener {
                    lastCheckedPosition = adapterPosition
                    notifyDataSetChanged()

                    listener.getDatafromBottomSheet(data)
                    if (radio_umrah_home_page_bottom_sheet.isChecked)
                        radio_umrah_home_page_bottom_sheet.isChecked = false
                    else {
                        radio_umrah_home_page_bottom_sheet.isChecked = true
                    }

                    CoroutineScope(Dispatchers.Main).launch{
                        delay(250)
                        bottomSheetDialog.dismiss()
                    }
                }
            }
        }
    }
    fun setList(list: UmrahHomepageBottomSheetData){
        listBottomSheet = list.list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = listBottomSheet.size

    override fun onBindViewHolder(holder: UmrahHomepageBottomSheetViewHolder, position: Int) {
        val current = listBottomSheet[position]
        holder.bind(current)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepageBottomSheetViewHolder {
        val item = inflater.inflate(R.layout.item_umrah_home_page_bottom_sheet, parent, false)
        return UmrahHomepageBottomSheetViewHolder(item)
    }

    interface UmrahBottomSheetListener{
        fun  getDatafromBottomSheet(data:UmrohHomepageBottomSheetwithType)
    }
}