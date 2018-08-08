package com.tokopedia.gamification.cracktoken.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tokopedia.gamification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 12/04/18.
 */

public class ExpiredCrackResult extends CrackResult {

    public ExpiredCrackResult(Context context, CrackResultStatus crackResultStatus) {

        setBenefitLabel(context.getString(R.string.expired_reward_title));

        List<CrackBenefit> crackBenefits = new ArrayList<>();
        CrackBenefit crackBenefit = new CrackBenefit();
        crackBenefit.setText(context.getString(R.string.expired_reward_message));
        crackBenefit.setColor(context.getString(R.string.expired_reward_color));
        crackBenefit.setSize(context.getString(R.string.expired_reward_size));
        crackBenefits.add(crackBenefit);

        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.image_error_crack_result_expired);

        CrackButton returnButton = new CrackButton();
        returnButton.setTitle(context.getString(R.string.ok_button));
        returnButton.setType(CrackResult.TYPE_BTN_DISMISS);

        setBenefits(crackBenefits);
        setImageBitmap(errorBitmap);
        setReturnButton(returnButton);

        setResultStatus(crackResultStatus);
    }
}
