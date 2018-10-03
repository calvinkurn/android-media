package com.tokopedia.imagepickerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListWidgetFragment;

/**
 * Created by hendry on 03/10/18.
 */
public class FragmentMerchantActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_test);

        MerchantVoucherListWidgetFragment fragment = (MerchantVoucherListWidgetFragment)
                getSupportFragmentManager().findFragmentById(R.id.merchantVoucherListWidgetFragment);
        if (fragment!= null) {
            String tkpdqc47shop = "394715";
            fragment.setShopId(tkpdqc47shop);
            fragment.setTitleString("Promo");
            fragment.loadAllData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100) {
//            if (data != null && resultCode == Activity.RESULT_OK) {
//                ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
//                if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
//                    File imgFile = new File(imageUrlOrPathList.get(0));
//                    if (imgFile.exists()) {
//                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                        imageView.setImageBitmap(myBitmap);
//                    }
//                }
//            }
//        }
    }
}
