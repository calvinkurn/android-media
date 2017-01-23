//package com.tokopedia.core;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.tokopedia.core.analytics.AppScreen;
//import com.tokopedia.core.app.TkpdActivity;
//import com.tokopedia.core.fragment.FragmentBuyCredit;
//
//
//public class BuyCredit extends TkpdActivity {
//
//    public static Intent createIntent(Context context){
//        Intent intent = new Intent(context, BuyCredit.class);
//        return intent;
//    }
//
//    @Override
//    public String getScreenName() {
//        return AppScreen.SCREEN_CREDIT_CARD_BUY;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        inflateView(R.layout.activity_buy_credit);
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new FragmentBuyCredit())
//                    .commit();
//        }
//    }
//
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_buy_credit, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        int shopId = item.getItemId();
////        if (shopId == R.shopId.action_settings) {
////            return true;
////        }
////        return super.onOptionsItemSelected(item);
////    }
//
//}
