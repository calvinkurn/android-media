package com.tokopedia.logisticgeolocation.pinpoint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.PredictionResult;
import com.tokopedia.logisticgeolocation.R;
import com.tokopedia.logisticgeolocation.data.IMapsRepository;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSession;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fajar Ulin Nuha on 29/10/18.
 */
public class SuggestionLocationAdapter extends ArrayAdapter<PredictionResult>
        implements Filterable {

    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    /**
     * Current results returned by this adapter.
     */
    private List<PredictionResult> mResultList;

    private CompositeSubscription compositeSubscription;

    private OnQueryListener queryListener;

    private IMapsRepository mapsRepository;

    /**
     * Handles autocomplete requests.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds mBounds;
    private UserSession mUser;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter mPlaceFilter;

    /**
     * Initializes with a resource for text rows and autocomplete query bounds.
     *
     * @see android.widget.ArrayAdapter#ArrayAdapter(android.content.Context, int)
     */
    public SuggestionLocationAdapter(Context context, GoogleApiClient googleApiClient,
                                     LatLngBounds bounds, AutocompleteFilter filter,
                                     UserSession userSession,
                                     CompositeSubscription compositeSubscription,
                                     IMapsRepository repository) {
        super(context, R.layout.layout_autocomplete_search_location, android.R.id.text1);
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mUser = userSession;
        mPlaceFilter = filter;
        mResultList = new ArrayList<>();
        this.mapsRepository = repository;
        this.compositeSubscription = compositeSubscription;
        this.compositeSubscription.add(Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                queryListener = new OnQueryListener() {
                    @Override
                    public void onQuerySubmit(String query) {
                        subscriber.onNext(String.valueOf(query));
                    }
                };
            }
        }).debounce(700, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(queryAutoCompleteSubscriber()));
    }

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        return mResultList.size();
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public PredictionResult getItem(int position) {
        return mResultList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        PredictionResult item = getItem(position);
        if (item != null) {
            TextView textView1 = row.findViewById(android.R.id.text1);
            TextView textView2 = row.findViewById(android.R.id.text2);
            textView1.setText(Html.fromHtml(item.getMainTextFormatted()));
            textView2.setText(Html.fromHtml(item.getSecondaryTextFormatted()));
        }
        return row;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    if (constraint.length() >= 3) {
                        // Query the autocomplete API for the (constraint) search string.
                        //TODO This is where listener is initiated
                        CommonUtils.dumper("PORING Masuk get Filter cuy");
                        if (queryListener != null) {
                            queryListener.onQuerySubmit(constraint.toString());
                        }
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                CommonUtils.dumper("PORING Publish Result");
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof PredictionResult) {
                    return ((PredictionResult) resultValue).getMainText();
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * objects to store the Place ID and description that the API returns.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see Places#GEO_DATA_API#getAutocomplete(CharSequence)
     * @see AutocompletePrediction#freeze()
     */
    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            CommonUtils.dumper("Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                String noConnection = getContext().getResources().getString(R.string.msg_no_connection) + ".\n"
                        + getContext().getResources().getString(R.string.error_no_connection2) + ".";
                Snackbar snackbar = SnackbarManager.make((Activity) getContext(), noConnection, Snackbar.LENGTH_LONG);
                if (!snackbar.isShownOrQueued()) {
                    KeyboardHandler.DropKeyboard(getContext(), ((Activity) getContext()).findViewById(android.R.id.content));
                    snackbar.show();
                }
                CommonUtils.dumper("Error contacting API: " + status.toString());
                autocompletePredictions.release();
                return new ArrayList<>();
            }

            CommonUtils.dumper("Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        CommonUtils.dumper("Google API client is not connected for autocomplete query.");
        return new ArrayList<>();
    }

    private Subscriber<String> queryAutoCompleteSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String query) {
                CommonUtils.dumper("PORING Kirim Data " + query);
                Map<String, String> temp = new HashMap<>();
                temp = AuthUtil.generateParamsNetwork(mUser.getUserId(), mUser.getDeviceId(), temp);
                TKPDMapParam<String, Object> params = new TKPDMapParam<>();
                params.put("input", query);
                params.putAll(temp);

                compositeSubscription.add(mapsRepository
                        .getAutoCompleteList(params, query)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<AutoCompleteViewModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e instanceof RuntimeException) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                } else if (e instanceof UnknownHostException) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                            @Override
                            public void onNext(AutoCompleteViewModel response) {
                                CommonUtils.dumper("PORING Terima Result");
                                mResultList = response.getListOfPredictionResults();
                                notifyDataSetChanged();
                            }
                        }));
            }
        };
    }


}