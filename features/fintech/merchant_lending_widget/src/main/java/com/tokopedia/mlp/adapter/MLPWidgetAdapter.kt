package com.tokopedia.mlp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.mlp.contractModel.BodyItem
import com.tokopedia.mlp.contractModel.BottomSheetItem
import com.tokopedia.mlp.contractModel.Header
import com.tokopedia.mlp.contractModel.WidgetsItem
import kotlinx.android.synthetic.main.fragment_merchant_lending.view.*
import kotlinx.android.synthetic.main.mlp_box_layout.view.*
import kotlinx.android.synthetic.main.mlp_row_info.view.*


class MLPWidgetAdapter(private val boxList: List<WidgetsItem>, val context: Context) : RecyclerView.Adapter<MLPWidgetAdapter.ViewHolder>(), View.OnClickListener {


    override fun onClick(v: View?) {
        Toast.makeText(context, "Adapter", Toast.LENGTH_SHORT).show()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MLPWidgetAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_merchant_lending, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(boxList[position], position)
    }

    override fun getItemCount(): Int {
        return if (boxList == null) 0 else boxList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(widgetsItem: WidgetsItem, position: Int) {
            widgetsItem.header?.let {
                itemView.text_title.setTextAndCheckShow(it.title)
                ImageHandler.loadImage(context, itemView.header_logo, it.logo, R.color.grey_100)
                itemView.text_badge.setTextAndCheckShow(it.tag?.name)

                if (HexValidator.validate(it.tag?.color)) {
                    itemView.text_badge.background.setColorFilter(Color.parseColor(it.tag?.color), PorterDuff.Mode.SRC_ATOP)
                }

                if (it.sideToggle != null && it.sideText != null) {
                    renderSideTextHeader(it, position)
                } else if (it.sideToggle != null && it.sideText == null) {
                    renderSideToggleHeader(it)
                } else if (it.sideText != null && it.sideToggle == null)
                    renderSideTextHeader(it, position)

            }

            widgetsItem.body?.let {
                renderBodyInfo(it)
                renderBodyBox(it)
            }
        }

        private fun renderBottomSheet(bottomSheetItem: BottomSheetItem?) {


            var closeableBottomSheetDialog: CloseableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            var view: View = View.inflate(context, R.layout.mlp_bottomsheet, null)
            view.findViewById<TextView>(R.id.tv_title_bottomsheet).text = bottomSheetItem?.title
            view.findViewById<TextView>(R.id.tv_details_bottomsheet).text = bottomSheetItem?.text
            closeableBottomSheetDialog.setContentView(view)
            closeableBottomSheetDialog.show()
            closeableBottomSheetDialog.setCanceledOnTouchOutside(true)
        }

        private fun renderBodyBox(it: List<BodyItem?>?) {


            if (it?.get(0)?.boxes == null) {
                itemView.box_container.visibility = View.GONE

            } else {
                itemView.box_container.visibility = View.VISIBLE
                var boxTitle: String? = null
                var boxContent: String? = null
                var boxColor: String? = null
                var length: Int = it[0]?.boxes?.size!!


                for (item in 0..length - 1) {
                    boxTitle = it[0]?.boxes?.get(item)?.title
                    boxContent = it[0]?.boxes?.get(item)?.text
                    boxColor = it[0]?.boxes?.get(item)?.boxColor

                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val myView = inflater.inflate(R.layout.mlp_box_layout, null)
                    myView.text_body_title.text = boxTitle
                    myView.text_body_content.text = boxContent
                    myView.viewbody_background.background.setColorFilter(Color.parseColor(boxColor), PorterDuff.Mode.SRC_ATOP)
                    itemView.box_container.addView(myView)
                }

            }
        }

        private fun renderBodyInfo(it: List<BodyItem?>?) {

            if (it?.get(0)?.info == null) {
                itemView.info_container.visibility = View.GONE

            } else {
                itemView.info_container.visibility = View.VISIBLE
                var infolabel: String? = null
                var infovalue: String? = null
                var length: Int = it.get(0)?.info?.size!!


                for (item in 0..length - 1) {
                    infolabel = it[0]?.info?.get(item)?.label
                    infovalue = it[0]?.info?.get(item)?.value

                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val myView = inflater.inflate(R.layout.mlp_row_info, null)
                    myView.label.text = infolabel
                    myView.value.text = infovalue

                    itemView.info_container.addView(myView)
                }

            }

        }

        private fun renderSideToggleHeader(headerContent: Header) {


            itemView.flip_switch.visibility = View.VISIBLE
            itemView.flip_switch.displayedChild = 0

            var showToggle: Boolean? = null
            var statusToggle: Boolean? = null

            if (headerContent.sideToggle?.showToggle!!) {
                showToggle = headerContent.sideToggle.showToggle

            }
            if (showToggle == true) {
                if (headerContent.sideToggle.toggleStatus!!) {
                    statusToggle = headerContent.sideToggle.toggleStatus

                    itemView.switch_enable.visibility = View.VISIBLE
                    if (statusToggle == false) {
                        itemView.switch_enable.toggle()
                    } else
                        itemView.switch_enable.isChecked = false

                }
            }

        }

        private fun renderSideTextHeader(headerContent: Header, position: Int) {

            itemView.flip_switch.visibility = View.VISIBLE
            itemView.flip_switch.displayedChild = 1
            itemView.text_side.setTextAndCheckShow(headerContent.sideText?.text)



            itemView.text_side.setOnClickListener {
                if (URLUtil.isValidUrl(headerContent.sideText?.url)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(headerContent.sideText?.url))
                    context.startActivity(intent)


                } else {
                    itemView.text_side.tag = position
                    itemView.text_side.setOnClickListener {
                        val position = it.tag as Int
                        if (checkBottomSheetID(position)) {
                            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                            renderBottomSheet(boxList[position].bottomSheet?.get(0))
                        }
                    }


                }
            }
        }


        private fun checkBottomSheetID(position: Int): Boolean {

            if (boxList[position].bottomSheet!![0]?.id.equals(boxList[position].header?.sideText?.url)) {
                return true
            }
            return false
        }
    }

}

