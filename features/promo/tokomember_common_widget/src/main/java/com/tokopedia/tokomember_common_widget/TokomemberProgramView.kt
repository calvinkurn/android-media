package com.tokopedia.tokomember_common_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.unifyprinciples.Typography
import android.widget.FrameLayout
import com.tokopedia.tokomember_common_widget.model.TokomemberProgramCardModel
import com.tokopedia.tokomember_common_widget.util.MemberType
import com.tokopedia.unifycomponents.ImageUnify

class TokomemberProgramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val REQUEST_CODE = 121
        const val RESULT_CODE_OK = 1
    }

    lateinit var ivTime: ImageUnify
    lateinit var ivMemberStatistics: ImageUnify
    lateinit var programStatus: Typography
    lateinit var periodProgram: Typography
    lateinit var programStartDate: Typography
    lateinit var programStartTime: Typography
    lateinit var programEndDate: Typography
    lateinit var programEndTime: Typography
    lateinit var programMemberLabel: Typography
    lateinit var programMemberValue: Typography
    lateinit var programMemberTransaksiLabel: Typography
    lateinit var programMemberTransaksivalue: Typography


    @MemberType
    var shopType: Int = MemberType.PREMIUM

    init {
        View.inflate(context, R.layout.tm_program_view, this)
        initViews()
        setClicks()
    }

    private fun initViews() {
        ivTime = this.findViewById(R.id.ivTime)
        ivMemberStatistics = this.findViewById(R.id.ivMemberStatistics)
        programStatus = this.findViewById(R.id.programStatus)
        periodProgram = this.findViewById(R.id.periodProgram)
        programStartDate = this.findViewById(R.id.programStartDate)
        programStartTime = this.findViewById(R.id.programStartTime)
        programEndDate = this.findViewById(R.id.programEndDate)
        programEndTime = this.findViewById(R.id.programEndTime)
        programMemberLabel = this.findViewById(R.id.programMemberLabel)
        programMemberValue = this.findViewById(R.id.programMemberValue)
        programMemberTransaksiLabel = this.findViewById(R.id.programMemberTransaksiLabel)
        programMemberTransaksivalue = this.findViewById(R.id.programMemberTransaksivalue)
    }

    private fun setClicks() {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setData(

    ) {

    }

    private fun setShopCardData(tokomemberShopViewModel: TokomemberProgramCardModel) {

    }

    fun sendImpressionTrackerForPdp() {

    }
}