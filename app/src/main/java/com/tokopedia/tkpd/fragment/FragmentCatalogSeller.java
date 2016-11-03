package com.tokopedia.tkpd.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.NoResultHandler;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.customadapter.ExpandableListCatalogSellerAdapter;
import com.tokopedia.tkpd.network.apiservices.search.CatalogAWSService;
import com.tokopedia.tkpd.util.SlideOffViewHandler;
import com.tokopedia.tkpd.util.SlideOffViewHandler.SlideOffMotionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentCatalogSeller extends Fragment {
    private View view;
    private View viewFilterSort;
    private Activity context;
    private ExpandableListView expListView;
    private ExpandableListCatalogSellerAdapter listAdapter;
    private ArrayList<String> location = new ArrayList<>();
    private ArrayList<String> locationName = new ArrayList<>();
    private ArrayList<String> locationId = new ArrayList<>();
    private ArrayList<String> mListSellerCatalog = new ArrayList<>();
    private HashMap<String, List<String>> mListSellerProduct = new HashMap<>();
    private TextView SortBut;
    private AlertDialog SortDialog;
    private String Sort = null;
    private ArrayList<String> SortValue;
    private String[] SortValueList;
    private AlertDialog.Builder SortMenu;

    private TextView FilterBut;
    private TextView filterLocation;
    private AlertDialog FilterDialog;
    private String Filter = null;
    private ArrayList<String> FilterValue;
    private String[] FilterValueList;
    private AlertDialog.Builder FilterMenu;
    private ArrayList<String> mListShop = new ArrayList<String>();
    private String CatalogSeller;
    private String LatestMp;
    private Boolean loading = false;
    private String CtgId;
    private String selectedLocId = "";
    private View footerLV;
    private View footerRetry;
    private NoResultHandler noResult;
    private SlideOffViewHandler SOVHandler = new SlideOffViewHandler();
    private boolean canHideHeader = true;
    private int page = 1;
    private Subscription getSellerListSubs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location.add(getString(R.string.title_all_location));
        locationId.add("");
        locationName.add(getString(R.string.title_all_location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fragment_catalog_seller, container, false);
        viewFilterSort = view.findViewById(R.id.filter_sort_btn);
        footerLV = View.inflate(context, R.layout.footer_list_view, null);
        footerRetry = View.inflate(context, R.layout.design_retry_footer, null);
        SortValueList = getResources().getStringArray(R.array.sort_browse_catalog_valuey_without_rating);
        SortValue = new ArrayList<String>(Arrays.asList(SortValueList));
        SortMenu = new AlertDialog.Builder(context);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.sort_browse_catalog_without_rating, android.R.layout.select_dialog_item);
        SortMenu.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.i("which_id", Integer.toString(which));
                Sort = SortValue.get(which);
                reloadData();
            }
        });

        FilterValueList = getResources().getStringArray(R.array.filter_browse_catalog_value);
        FilterValue = new ArrayList<String>(Arrays.asList(FilterValueList));
        FilterMenu = new AlertDialog.Builder(context);
        ArrayAdapter<CharSequence> adapterF = ArrayAdapter.createFromResource(context, R.array.filter_browse_catalog, android.R.layout.select_dialog_item);
        FilterMenu.setAdapter(adapterF, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.i("which_id", Integer.toString(which));
                Filter = FilterValue.get(which);
                reloadData();
            }
        });
        expListView = (ExpandableListView) view.findViewById(R.id.lvExpSeller);

        SortDialog = SortMenu.create();
        SortBut = (TextView) view.findViewById(R.id.sort_but);

        SortBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SortDialog.show();
            }

        });

        FilterDialog = FilterMenu.create();
        FilterBut = (TextView) view.findViewById(R.id.filter_but);
        FilterBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FilterDialog.show();
            }

        });

        filterLocation = (TextView) view.findViewById(R.id.location);
        filterLocation.setOnClickListener(onLocationFilterClick());

        noResult = new NoResultHandler(context, expListView);
        expListView.setGroupIndicator(null);
        listAdapter = new ExpandableListCatalogSellerAdapter(context, mListSellerCatalog, mListSellerProduct);

        // setting list adapter
        expListView.addFooterView(footerLV);
        expListView.setAdapter(listAdapter);
        expListView.removeFooterView(footerLV);
        noResult.tempRemoveHeader();
        footerRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });
        expListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                if (arg1 + arg2 == arg3 && arg3 != 0) {
                    if (!loading && page > 0) {
                        page += 1;
                        expListView.addFooterView(footerLV);
                        loadCatalogSeller();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

        });
        expListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (canHideHeader)
                    SOVHandler.MotionEventListener(event, new SlideOffMotionEventListener() {
                        @Override
                        public void OnMoveUp() {
                            SOVHandler.ToggleSlideOffScreen(viewFilterSort, false, true);
//							SOVHandler.ToggleSlideOffScreen(marketPrice, true, true);
                        }

                        @Override
                        public void OnMoveDown() {
                            SOVHandler.ToggleSlideOffScreen(viewFilterSort, false, false);
//							SOVHandler.ToggleSlideOffScreen(marketPrice, true, false);
                        }
                    });
                return false;
            }
        });
        reloadData();
        return view;
    }

    private OnClickListener onLocationFilterClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                createLocationFilterDialog();
            }
        };
    }

    private void createLocationFilterDialog() {
        AlertDialog.Builder locationDialog = new AlertDialog.Builder(getActivity());
        SimpleSpinnerAdapter adapter = SimpleSpinnerAdapter.createAdapter(getActivity(), location, android.R.layout.select_dialog_item);
        locationDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterLocation.setText(locationName.get(which));
                selectedLocId = locationId.get(which);
                reloadData();
            }
        });
        locationDialog.create().show();
    }

    @Override
    public void onAttach(Activity activity) {
        context = getActivity();

        Bundle bundle = this.getArguments();
        CtgId = bundle.getString("ctg_id");
        LatestMp = bundle.getString("latest_mp");
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void retry() {
        loading = true;
        expListView.removeFooterView(footerRetry);
        expListView.addFooterView(footerLV);
        loadCatalogSeller();
    }

    private void reloadData() {
        if (loading)
            return;
        page = 1;
        mListShop.clear();
        mListSellerCatalog.clear();
        mListSellerProduct.clear();

        expListView.addFooterView(footerLV);
        listAdapter = new ExpandableListCatalogSellerAdapter(context, mListSellerCatalog, mListSellerProduct);
        expListView.setAdapter(listAdapter);
        loadCatalogSeller();
    }

    private void loadCatalogSeller() {
        loading = true;
        noResult.removeMessage();
        CatalogAWSService catalogService = new CatalogAWSService();
        Observable<Response<String>> observable = catalogService.getApi().getSellerListLocation(CtgId, getPage(page), 10, Sort, Filter, selectedLocId);
        getSellerListSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetSellerSubscribe());
    }

    private Subscriber<Response<String>> onGetSellerSubscribe() {
        return new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {
                loading = false;
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                expListView.removeFooterView(footerLV);
                expListView.addFooterView(footerRetry);
            }

            @Override
            public void onNext(Response<String> tkpdResponseResponse) {
                expListView.removeFooterView(footerLV);
                CatalogSeller = tkpdResponseResponse.body();
                try {
                    getCatalogModels();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getCatalogModels() throws JSONException {
        if (page <= 1){
            resetLocation();
        }
        JSONObject result = new JSONObject(CatalogSeller);
        JSONArray catalogList = result.getJSONArray("data");
        JSONObject catalog;
        JSONArray locationList = result.getJSONArray("locations");

        int prodNum;

        int catalogNum = catalogList.length();
        if (locationList.length() > 0) {
            loadLocation(locationList);
        }
        if (catalogNum > 0) {
            for (int i = 0; i < catalogNum; i++) {
                ArrayList<String> productListString = new ArrayList<>();
                catalog = catalogList.getJSONObject(i);
                JSONArray productList = catalog.getJSONArray("products");
                prodNum = productList.length();
                for (int j = 0; j < prodNum; j++) {
                    productListString.add(productList.getString(j));
                }
                mListSellerProduct.put(Integer.toString(mListSellerCatalog.size()), productListString);
                mListSellerCatalog.add(catalog.getString("shop"));
            }
        } else {
            page = -1;
            if (mListSellerCatalog.size() == 0)

                noResult.showMessage();
        }
        listAdapter.notifyDataSetChanged();
    }

    private void loadLocation(JSONArray locationList) throws JSONException {// TODO
        JSONObject locationJSON;
        int locationNum = locationList.length();
        String name = "";
        for (int i = 0; i < locationNum; i++) {
            locationJSON = locationList.getJSONObject(i);
            name = locationJSON.getString("name");
            name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            locationName.add(name);
            location.add(name + " (" + locationJSON.getString("total_data") + ")");
            locationId.add(locationJSON.getString("id"));
        }
    }

    private int getPage(int page) {
        return 10 * (page - 1);
    }

    private void resetLocation() {
        location.clear();
        locationId.clear();
        locationName.clear();
        location.add(getString(R.string.title_all_location));
        locationId.add("");
        locationName.add(getString(R.string.title_all_location));
    }

}
