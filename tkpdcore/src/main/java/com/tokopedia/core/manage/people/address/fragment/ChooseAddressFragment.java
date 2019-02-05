package com.tokopedia.core.manage.people.address.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.adapter.ChooseAddressAdapter;
import com.tokopedia.core.manage.people.address.listener.ChooseAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.presenter.ChooseAddressFragmentPresenter;
import com.tokopedia.core.manage.people.address.presenter.ChooseAddressFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressFragment extends BasePresenterFragment<ChooseAddressFragmentPresenter> implements ChooseAddressFragmentView,
        ChooseAddressActivity.OnChooseAddressViewListener {

    @BindView(R2.id.main_view)
    View mainView;

    @BindView(R2.id.address_list)
    RecyclerView addressRV;

    @BindView(R2.id.search)
    EditText search;

    @BindView(R2.id.search_but)
    ImageView searchBut;

    ChooseAddressAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;
    Snackbar snackbar;


    public static ChooseAddressFragment createInstance() {
        ChooseAddressFragment fragment = new ChooseAddressFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ChooseAddressFragment createInstance(Bundle bundle) {
        ChooseAddressFragment fragment = new ChooseAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        setActionsEnabled(false);
        presenter.setCache(search.getText() != null ? search.getText().toString() : "");

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ChooseAddressFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_choose_address;
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh(search.getText() != null ? search.getText().toString() : "");
            }
        };
    }

    @Override
    protected void initView(View view) {
        this.refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        snackbar = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_INDEFINITE);
        searchBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refresh();
                KeyboardHandler.DropKeyboard(context, search);
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refresh();
                KeyboardHandler.DropKeyboard(context, search);
                return true;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    search.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    refresh();
                } else {
                    search.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_24dp, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if (search.getText() != null && search.getText().length() > 0) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (search.getRight() -
                                search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            search.setText("");
                            refresh();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem, search.getText() != null ? search.getText().toString() : "");
            }
        };
    }

    @Override
    protected void setViewListener() {
        addressRV.addOnScrollListener(onScroll());
    }

    @Override
    protected void initialVar() {

        adapter = ChooseAddressAdapter.createInstance(getActivity(), presenter);
        adapter.setOnRetryListenerRV(presenter.onRetry(search.getText() != null ? search.getText().toString() : ""));
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addressRV.setLayoutManager(linearLayoutManager);
        addressRV.setAdapter(adapter);

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void finishLoading() {
        adapter.showLoading(false);
        adapter.showEmpty(false);
        refreshHandler.setPullEnabled(true);
        refreshHandler.setRefreshing(false);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }


    @Override
    public ChooseAddressAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void showErrorMessage(String s) {
        snackbar = SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_close), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }
                );
        snackbar.show();
    }

    @Override
    public void removeError() {
        snackbar.dismiss();
        adapter.showEmpty(false);
    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
    }

    @Override
    public boolean isRefreshing() {
        return refreshHandler.isRefreshing();
    }

    @Override
    public void refresh() {
        presenter.onRefresh(search.getText() != null ? search.getText().toString() : "");
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void showEmptyState() {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText() != null ? search.getText().toString() : "");
            }
        });
    }

    @Override
    public void setRetry() {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText() != null ? search.getText().toString() : "");
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText() != null ? search.getText().toString() : "");
            }
        });
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setCache(search.getText() != null ? search.getText().toString() : "");
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity().getIntent().getBooleanExtra("resolution_center", false)) {
            inflater.inflate(R.menu.manage_people_address, menu);
        } else {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_address) {
            presenter.setOnAddAddressClick(getActivity());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    presenter.onSuccessCreateAddress();
                    break;
                case ChooseAddressFragmentPresenterImpl.REQUEST_CHOOSE_ADDRESS_CODE:
                    presenter.onSuccessEditAddress();
                default:
                    break;
            }
        }
    }

    @Override
    public void resetSearch() {
        search.setText("");
    }

    @Override
    public void navigateToAddAddress(Bundle bundle) {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @Override
    public void navigateToEditAddress(Bundle bundle) {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ChooseAddressFragmentPresenterImpl.REQUEST_CHOOSE_ADDRESS_CODE);
    }


    @Override
    public ArrayList<Destination> onActivityBackPressed() {
        return getAdapter().getList();
    }
}
