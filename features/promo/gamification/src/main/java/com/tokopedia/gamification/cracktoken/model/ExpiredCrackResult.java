package com.tokopedia.gamification.cracktoken.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackButtonEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.ResultStatusEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 12/04/18.
 */

public class ExpiredCrackResult extends CrackResultEntity {

    public ExpiredCrackResult(Context context, ResultStatusEntity crackResultStatus) {

        setBenefitLabel(context.getString(R.string.expired_reward_title));

        List<CrackBenefitEntity> crackBenefits = new ArrayList<>();
        CrackBenefitEntity crackBenefit = new CrackBenefitEntity();
        crackBenefit.setText(context.getString(R.string.expired_reward_message));
        crackBenefit.setColor(context.getString(R.string.expired_reward_color));
        crackBenefit.setSize(context.getString(R.string.expired_reward_size));
        crackBenefits.add(crackBenefit);

        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.image_error_crack_result_expired);

        CrackButtonEntity returnButton = new CrackButtonEntity();
        returnButton.setTitle(context.getString(R.string.ok_button));
        returnButton.setType(CrackResultEntity.TYPE_BTN_DISMISS);

        setBenefits(crackBenefits);
        setImageBitmap(errorBitmap);
        setReturnButton(returnButton);

        setResultStatus(crackResultStatus);
    }
}
