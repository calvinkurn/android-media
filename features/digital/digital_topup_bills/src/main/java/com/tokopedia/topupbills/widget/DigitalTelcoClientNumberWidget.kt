package com.tokopedia.digital.topupbillsproduct.compoundview

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 26/04/19.
 */
class DigitalTelcoClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : DigitalBaseClientNumberWidget(context, attrs, defStyleAttr) {

    override fun getLayout(): Int {
        return R.layout.view_digital_input_number_telco
    }


}
