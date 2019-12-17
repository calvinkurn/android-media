package com.tokopedia.opportunity.common.showcase;

import com.tokopedia.opportunity.R;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;

public class ShowCaseDialogFactory {
    public static ShowCaseDialog createTkpdShowCase (){
        return new ShowCaseBuilder()
                .titleTextColorRes(com.tokopedia.design.R.color.white)
                .spacingRes(com.tokopedia.core2.R.dimen.spacing_show_case)
                .arrowWidth(com.tokopedia.core2.R.dimen.arrow_width_show_case)
                .textColorRes(com.tokopedia.design.R.color.grey_400)
                .shadowColorRes(com.tokopedia.showcase.R.color.shadow)
                .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                .textSizeRes(com.tokopedia.core2.R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(com.tokopedia.showcase.R.drawable.selector_circle_green)
                .prevStringRes(R.string.label_back)
                .nextStringRes(com.tokopedia.showcase.R.string.next)
                .finishStringRes(com.tokopedia.core2.R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(true)
                .skipStringRes(com.tokopedia.core2.R.string.title_skip_2)
                .build();
    }
}