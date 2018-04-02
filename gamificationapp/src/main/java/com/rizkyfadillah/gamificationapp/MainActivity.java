package com.rizkyfadillah.gamificationapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.gamification.cracktoken.presentation.fragment.CrackTokenFragment;
import com.tokopedia.gamification.floating.view.model.TokenAsset;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg.png");
        imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg-crack-4.png");
        imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg-right.png");
        imageUrls.add("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-special/special-egg-left.png");

        TokenAsset tokenAsset = new TokenAsset();
        tokenAsset.setImageUrls(imageUrls);
        tokenAsset.setSmallImgUrl("https://ecs7.tokopedia.net/assets/images/promo/tokopoints/egg-icon/032018-float/plat1.png");

        TokenUser tokenUser = new TokenUser();
        tokenUser.setTokenUserID(1);
        tokenUser.setBackgroundImgUrl("https://ecs7.tokopedia.net/assets/images/gamification/background/bg-special-egg--tiny.png");
        tokenUser.setTokenAsset(tokenAsset);
        tokenUser.setTimeRemainingSeconds(3000);

        Fragment crackTokenFragment = CrackTokenFragment.newInstance(new TokenData());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, crackTokenFragment);
        fragmentTransaction.commit();
    }
}
