package com.tokopedia.core.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.tokopedia.core.EtalaseShopEditor;
import com.tokopedia.core.ManageShopAddress;
import com.tokopedia.core.ManageShopNotes;
import com.tokopedia.core.PaymentEditor;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.core.shipping.EditShippingActivity;
import com.tokopedia.core.shop.ShopEditorActivity;

public class FragmentSettingShop extends Fragment{
	private SimpleListTabViewAdapter lvAdapter;
	private ListView lvManage;
	private ArrayList<String> Name = new ArrayList<String>();
	private ArrayList<Integer> ResID = new ArrayList<Integer>();

	public static FragmentSettingShop newInstance() {
		return new FragmentSettingShop();
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_manage_general, container, false);
		Name.clear();
		ResID.clear();
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
		lvManage = (ListView) mainView.findViewById (R.id.list_manage);
		lvAdapter = new SimpleListTabViewAdapter(getActivity(), Name, ResID);
		lvManage.setAdapter(lvAdapter);
		lvManage.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = null;
				switch(pos) {
				case 0:
					intent = new Intent(getActivity(), ShopEditorActivity.class);
					intent.putExtra(ShopEditorActivity.FRAGMENT_TO_SHOW, ShopEditorActivity.EDIT_SHOP_FRAGMENT_TAG);
					startActivityForResult(intent, 0);
					break;
				case 1:
					intent = new Intent(getActivity(), EditShippingActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(getActivity(), PaymentEditor.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(getActivity(), EtalaseShopEditor.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(getActivity(), ManageShopNotes.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(getActivity(), ManageShopAddress.class);
					startActivity(intent);
					break;
				}
			}
			
		});
		return mainView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && isAdded() && getActivity() !=null) {
			ScreenTracking.screen(this);
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
}
	
