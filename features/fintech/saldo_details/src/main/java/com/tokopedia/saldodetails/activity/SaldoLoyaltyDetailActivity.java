//package com.tokopedia.saldodetails.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
//import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
//import com.tokopedia.saldodetails.R;
//import com.tokopedia.saldodetails.analytics.SaldoEventConstant;
//
//public class SaldoLoyaltyDetailActivity extends BaseSimpleActivity {
//
//
//    public static int FRAGMENT_VIEW = R.id.main_view;
//    private String url;
//
//    public static Intent createInstance(Context context, Bundle bundle) {
//        Intent intent = new Intent(context, SaldoLoyaltyDetailActivity.class);
//        intent.putExtras(bundle);
//        return intent;
//    }
//
//    @Override
//    public String getScreenName() {
//        return SaldoEventConstant.ScreenName.SCREEN_LOYALTY;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        inflateView(R.layout.activity_loyalty_detail);
//        url = getIntent().getExtras().getString("url");
//        if (url != null && url.contains("https://pulsa.tokopedia.com/saldo/"))
//            getSupportActionBar().setTitle("Saldo Saya");
//        else
//            getSupportActionBar().setTitle("Detail TopPoints");
//
//    }
//
//
//    @Override
//    protected Fragment getNewFragment() {
//        return BaseSessionWebViewFragment.newInstance(url);
//    }
//
//
//}
