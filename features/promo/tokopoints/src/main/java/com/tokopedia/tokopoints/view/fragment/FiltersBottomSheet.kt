package com.tokopedia.tokopoints.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.CatalogFilterPointRange
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.unifycomponents.BottomSheetUnify

class FiltersBottomSheet : BottomSheetUnify() {
    private var mFilterDetails: List<CatalogFilterPointRange?>? = null
    private var onSaveFilterCallback: OnSaveFilterCallback? = null
    private var fromInitView = true
    private var lastCheckedPosition = Int.MIN_VALUE
    private var lastSavedPosition = Int.MIN_VALUE

    interface OnSaveFilterCallback {
        fun onSaveFilter(filter: CatalogFilterPointRange?, selectedPosition: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    private fun initBottomSheet() {
        setTitle(getString(R.string.title_bottomsheet_filters))
        val view = View.inflate(requireContext(), R.layout.tp_bottomsheet_filters, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        fromInitView = true
        val rvFilters: RecyclerView = view.findViewById(R.id.rv_filters)
        val btnSave = view.findViewById<TextView>(R.id.btn_save)
        btnSave.setOnClickListener(View.OnClickListener {
            if (mFilterDetails == null) {
                return@OnClickListener
            }
            if (lastCheckedPosition != Int.MIN_VALUE) {
                if (lastSavedPosition != Int.MIN_VALUE) mFilterDetails!![lastSavedPosition]!!.isSelected = false
                lastSavedPosition = lastCheckedPosition
                onSaveFilterCallback!!.onSaveFilter(mFilterDetails!![lastSavedPosition], lastSavedPosition)
                mFilterDetails!![lastSavedPosition]!!.isSelected = true
            } else onSaveFilterCallback!!.onSaveFilter(null, lastSavedPosition)
            dismiss()
            AnalyticsTrackerUtil.sendEvent(activity,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_SAVE_FILTER,
                    mFilterDetails!![lastSavedPosition]!!.text)
        })
        rvFilters.adapter = FiltersAdapter(mFilterDetails)
    }

    fun setData(filterDetails: List<CatalogFilterPointRange?>?, onSaveFilterCallback: OnSaveFilterCallback?) {
        mFilterDetails = filterDetails
        this.onSaveFilterCallback = onSaveFilterCallback
    }

    inner class FiltersAdapter(private val mFilterDetails: List<CatalogFilterPointRange?>?) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = View.inflate(parent.context, R.layout.item_catalog_filters, null)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (!TextUtils.isEmpty(mFilterDetails?.get(position)?.text)) holder.radioButton.text = mFilterDetails?.get(position)?.text
            if (fromInitView) {
                holder.radioButton.isChecked = mFilterDetails?.get(position)?.isSelected ?: false
            } else {
                holder.radioButton.isChecked = position == lastCheckedPosition
            }
        }

        override fun getItemCount(): Int {
            return mFilterDetails!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var radioButton: RadioButton

            init {
                radioButton = itemView.findViewById(R.id.rb_filter)
                radioButton.setOnClickListener {
                    lastCheckedPosition = adapterPosition
                    fromInitView = false
                    try {
                        if (mFilterDetails != null && mFilterDetails[adapterPosition] != null) {
                            AnalyticsTrackerUtil.sendEvent(activity,
                                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                                    AnalyticsTrackerUtil.ActionKeys.PILIH_FILTER,
                                    mFilterDetails[adapterPosition]?.text)
                        }
                    } catch (e: Exception) {
                    }
                    notifyDataSetChanged()
                }
            }
        }

        init {
            val size = itemCount
            for (i in 0 until size) {
                if (mFilterDetails != null) {
                    if (mFilterDetails.get(i)?.isSelected!!) {
                        lastCheckedPosition = i
                        lastSavedPosition = i
                        break
                    }
                }
            }
        }
    }
}