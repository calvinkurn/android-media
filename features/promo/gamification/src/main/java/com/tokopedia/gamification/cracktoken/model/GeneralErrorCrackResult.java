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

public class GeneralErrorCrackResult extends CrackResult {

    public GeneralErrorCrackResult(Context context) {

        setBenefitLabel("Maaf, sayang sekali sepertinya");

        List<CrackBenefit> crackBenefits = new ArrayList<>();
        crackBenefits.add(new CrackBenefit(context.getString(R.string.crack_token_got_technical_difficulties),
                "#ffffff", "medium"));

        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_error_crack_result);

        CrackButton returnButton = new CrackButton();
        returnButton.setTitle("Coba Lagi");
        returnButton.setType("dismiss");

        setBenefits(crackBenefits);
        setImageBitmap(errorBitmap);
        setReturnButton(returnButton);

        CrackResultStatus crackResultStatus = new CrackResultStatus();
        crackResultStatus.setCode("500");
        setResultStatus(crackResultStatus);
    }
}
