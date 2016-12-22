package com.tokopedia.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.manage.shop.notes.activity.ManageShopNotesActivity;
import com.tokopedia.core.shipping.EditShippingActivity;

import java.util.ArrayList;

public class ManageShop extends TkpdActivity {

    private SimpleListTabViewAdapter lvAdapter;
    private ListView lvManage;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<Integer> ResID = new ArrayList<Integer>();

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_SETTING_MANAGE_SHOP;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_manage_shop);


        Name.add(getString(R.string.title_shop_information_menu));
        Name.add(getString(R.string.title_shipping_menu));
        Name.add(getString(R.string.title_payment_menu));
        Name.add(getString(R.string.title_etalase_menu));
        Name.add(getString(R.string.title_notes_menu));
        Name.add(getString(R.string.title_location_menu));
        ResID.add(R.drawable.ic_set_shop_info);
        ResID.add(R.drawable.ic_set_shipping);
        ResID.add(R.drawable.ic_set_payment);
        ResID.add(R.drawable.ic_set_etalase);
        ResID.add(R.drawable.ic_set_notes);
        ResID.add(R.drawable.ic_set_location);
        lvManage = (ListView) findViewById(R.id.list_manage);
        lvAdapter = new SimpleListTabViewAdapter(ManageShop.this, Name, ResID);
        lvManage.setAdapter(lvAdapter);
        lvManage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = null;
				switch(pos) {
				case 0:
					intent = SellerRouter.getAcitivityShopCreateEdit(ManageShop.this);
					intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
							SellerRouter.ShopSettingConstant.EDIT_SHOP_FRAGMENT_TAG);
					UnifyTracking.eventManageShopInfo();
					startActivityForResult(intent, 0);
					break;
				case 1:
					intent = new Intent(ManageShop.this, EditShippingActivity.class);
					UnifyTracking.eventManageShopShipping();
					startActivity(intent);
					break;
				case 2:
					UnifyTracking.eventManageShopPayment();
					intent = new Intent(ManageShop.this, PaymentEditor.class);
					startActivity(intent);
					break;
				case 3:
					UnifyTracking.eventManageShopEtalase();
					intent = new Intent(ManageShop.this, EtalaseShopEditor.class);
					startActivity(intent);
					break;
				case 4:
					UnifyTracking.eventManageShopNotes();
					intent = new Intent(ManageShop.this, ManageShopNotesActivity.class);
					startActivity(intent);
					break;
				case 5:
					UnifyTracking.eventManageShopLocation();
					intent = new Intent(ManageShop.this, ManageShopAddress.class);
					startActivity(intent);
					break;
				}
			}
			
		});
		
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK)
            super.RefreshDrawer();
        super.onActivityResult(requestCode, resultCode, data);
    }


}
