package com.tokopedia.tkpd.manage.people.address.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterActivity;
import com.tokopedia.tkpd.manage.people.address.ManageAddressConstant;
import com.tokopedia.tkpd.manage.people.address.fragment.ChooseAddressFragment;
import com.tokopedia.tkpd.manage.people.address.presenter.ChooseAddressFragmentPresenterImpl;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressActivity extends BasePresenterActivity {

    public static final String REQUEST_CODE_PARAM_ADDRESS = "ADDRESSPASSDATA";

    private static final String TAG = "CHOOSE_ADDRESS_FRAGMENT";

    public static Intent createInstance(Context context) {
        return new Intent(context, ChooseAddressActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
       return R.layout.activity_choose_address;
    }

    @Override
    protected void initView() {
        ChooseAddressFragment fragment = ChooseAddressFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ChooseAddressFragmentPresenterImpl.REQUEST_CHOOSE_ADDRESS_CODE && resultCode == Activity.RESULT_OK){
            ((ChooseAddressFragment)getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getBooleanExtra("resolution_center", false)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.manage_people_address, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_address:
                Intent intent = new Intent(getBaseContext(), AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_edit", false);
                intent.putExtras(bundle);
                startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
