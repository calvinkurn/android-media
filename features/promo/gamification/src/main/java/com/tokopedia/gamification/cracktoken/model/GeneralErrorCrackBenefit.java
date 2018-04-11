
package com.tokopedia.gamification.cracktoken.model;

import android.content.Context;

import com.tokopedia.gamification.R;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class GeneralErrorCrackBenefit extends CrackBenefit{

    public GeneralErrorCrackBenefit(Context context) {
        super(context.getString(R.string.crack_token_got_technical_difficulties),
                "#ffffff", "medium");
    }
}
