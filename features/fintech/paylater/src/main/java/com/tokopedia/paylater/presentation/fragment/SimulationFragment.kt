package com.tokopedia.paylater.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.SimulationTableResponse
import com.tokopedia.paylater.presentation.widget.PayLaterSignupBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.fragment_simulation.*

class SimulationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simulation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        populateRowHeaders()
        populateSimulationTable(context!!)
    }

    private fun computeSimulationData(): ArrayList<SimulationTableResponse> {
        val simulationData = ArrayList<SimulationTableResponse>()
        val installmentList = ArrayList<String>()
        for (i in 1..5) {
            if (i == 3) installmentList.add("-")
            else installmentList.add("Rp2.750.000")
        }
        for (i in 1..4) {
            simulationData.add(SimulationTableResponse(
                    "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                    installmentList))
        }
        return simulationData
    }

    private fun initListeners() {
        btnDaftarPayLater.setOnClickListener {
            PayLaterSignupBottomSheet.show(Bundle(), childFragmentManager)
        }
        paylaterDaftarWidget.setOnClickListener {
            PayLaterSignupBottomSheet.show(Bundle(), childFragmentManager)
        }
    }

    private fun populateRowHeaders() {
        context?.let {
            val layoutParam = ViewGroup.LayoutParams(it.dpToPx(84).toInt(), it.dpToPx(54).toInt())


            llPayLaterPartner.apply {
                for (i in 0..4) {
                    when (i) {
                        0 -> addView(getBlankView(layoutParam, it))
                        1 -> addView(getRecomView(layoutParam))
                        else -> addView(getNoRecomView(layoutParam, it, i%2 ==0))
                    }
                }
            }
        }
    }

    private fun getBlankView(layoutParam: ViewGroup.LayoutParams, context: Context): View {
        val rowHeaderBlank = LayoutInflater.from(context).inflate(R.layout.paylater_simulation_table_row_header, null)
        val parent = rowHeaderBlank.findViewById<ConstraintLayout>(R.id.clSimulationTableRowHeader)
        parent.setBackgroundColor(ContextCompat.getColor(context, R.color.Neutral_N50))
        parent.layoutParams = layoutParam
        val recomBadge = rowHeaderBlank.findViewById<ImageView>(R.id.ivRecommendationBadge)
        val image = rowHeaderBlank.findViewById<ImageView>(R.id.ivPaylaterPartner)
        image.gone()
        recomBadge.gone()
        return rowHeaderBlank
    }

    private fun getNoRecomView(layoutParam: ViewGroup.LayoutParams, context: Context, showBackGround: Boolean): View? {
        val rowHeaderNoRecom = LayoutInflater.from(context).inflate(R.layout.paylater_simulation_table_row_header, null)
        rowHeaderNoRecom.layoutParams = layoutParam
        val ivPaylaterPartner1 = rowHeaderNoRecom.findViewById<ImageUnify>(R.id.ivPaylaterPartner)
        ImageHandler.loadImage(context,
                ivPaylaterPartner1,
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
        if (showBackGround)
            rowHeaderNoRecom.setBackgroundColor(ContextCompat.getColor(context, R.color.Unify_N50))
        val recomBadge = rowHeaderNoRecom.findViewById<ImageView>(R.id.ivRecommendationBadge)
        recomBadge.gone()
        return rowHeaderNoRecom

    }

    private fun getRecomView(layoutParam: ViewGroup.LayoutParams): View? {
        val rowHeaderRecom = LayoutInflater.from(context).inflate(R.layout.paylater_simulation_table_row_header, null)
        rowHeaderRecom.layoutParams = layoutParam
        val ivPaylaterPartner2 = rowHeaderRecom.findViewById<ImageUnify>(R.id.ivPaylaterPartner)
        ImageHandler.loadImage(context,
                ivPaylaterPartner2,
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
        return rowHeaderRecom
    }

    private fun populateSimulationTable(context: Context) {
        val data = computeSimulationData()
        val rowCount = data.size + 1
        val colCount = data.getOrNull(0)?.installmentData?.size ?: 0
        val contentLayoutParam = TableRow.LayoutParams(context.dpToPx(110).toInt(), context.dpToPx(54).toInt())
        val tableLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT)
        for (i in 0 until rowCount) {
            val contentRow = TableRow(context)
            for (j in 0 until colCount) {
                when (i) {
                    0 -> contentRow.addView(getColumnHeader(contentLayoutParam, context, j), contentLayoutParam)
                    else -> contentRow.addView(getInstallmentView(contentLayoutParam, context, data[i-1].installmentData[j], i % 2 == 0), contentLayoutParam)
                }
            }
            tlInstallmentTable.addView(contentRow, tableLayoutParams)
        }
    }

    private fun getInstallmentView(contentLayoutParam: ViewGroup.LayoutParams, context: Context, priceText: String, showBackGround: Boolean): View {
        val installmentView = LayoutInflater.from(context).inflate(R.layout.paylater_simulation_table_content, null)
        installmentView.layoutParams = contentLayoutParam
        if (showBackGround)
            installmentView.setBackgroundColor(ContextCompat.getColor(context, R.color.Unify_N50))
        val tvInstallmentPrice = installmentView.findViewById<TextView>(R.id.tvInstallmentPrice)
        tvInstallmentPrice.text = priceText
        return installmentView
    }

    private fun getColumnHeader(contentLayoutParam: ViewGroup.LayoutParams, context: Context, position: Int): View {
        val installmentColumnHeader = LayoutInflater.from(context).inflate(R.layout.paylater_simulation_table_column_header, null)
        installmentColumnHeader.layoutParams = contentLayoutParam
        if(position != 0) {
            installmentColumnHeader.findViewById<TextView>(R.id.offerLabel).gone()
            installmentColumnHeader.findViewById<TextView>(R.id.tableHeader).text = "Cicil ${position*3}x"
        }

        return installmentColumnHeader
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                SimulationFragment()
    }
}