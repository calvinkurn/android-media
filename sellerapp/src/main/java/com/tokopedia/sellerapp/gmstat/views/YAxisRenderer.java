package com.tokopedia.sellerapp.gmstat.views;

import com.db.chart.renderer.StringFormatRenderer;
import com.tokopedia.sellerapp.gmstat.utils.KMNumbers2;

/**
 * Created by sebastianuskh on 12/20/16.
 */

public class YAxisRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String s) {
        return KMNumbers2.formatNumbers(Long.valueOf(s));
    }
}
