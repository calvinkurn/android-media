package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.library.baseadapter.BaseAdapter;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.PointHistoryBase;
import com.tokopedia.tokopoints.view.model.PointHistoryItem;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class PointHistoryListAdapter extends BaseAdapter<PointHistoryItem> {
    private final Context mContext;

    public PointHistoryListAdapter(Context context, AdapterCallback callback) {
        super(callback);
        this.mContext = context;
    }

    public class ViewHolder extends BaseVH {
        TextView txnId, title, note, date,
                point, loyalty;
        ImageView imgIcon;

        public ViewHolder(View view) {
            super(view);
            point = view.findViewById(R.id.text_point);
            loyalty = view.findViewById(R.id.text_loyalty);
            txnId = view.findViewById(R.id.text_txn_id);
            title = view.findViewById(R.id.text_title);
            note = view.findViewById(R.id.text_note);
            date = view.findViewById(R.id.text_date);
            imgIcon = view.findViewById(R.id.img_icon);
        }

        @Override
        public void bindView(PointHistoryItem item, int position) {
            setData(this, item, position);
        }
    }

    private void setData(ViewHolder holder, PointHistoryItem item, int position) {
        ImageHandler.loadImageFitCenter(holder.imgIcon.getContext(), holder.imgIcon, item.getIcon());
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getCreateTimeDesc());
        holder.txnId.setText(String.format("#%d", item.getId()));

        if (item.getNotes().isEmpty()) {
            holder.note.setText("");
        } else {
            holder.note.setText(item.getNotes());
        }

        if (item.getRewardPoints() != 0) {
            holder.point.setText(item.getRewardPoints() > 0
                    ? String.format(" +%d", item.getRewardPoints()) : String.format(" %d", item.getRewardPoints()));
        } else {
            holder.point.setText(" -");
        }

        if (item.getMemberPoints() != 0) {
            holder.loyalty.setText(item.getMemberPoints() > 0
                    ? String.format(" +%d", item.getMemberPoints()) : String.format(" %d", item.getMemberPoints()));
        } else {
            holder.loyalty.setText(" -");
        }
    }

    @Override
    protected BaseVH getItemViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_item_point_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void loadData(int currentPageIndex) {
        super.loadData(currentPageIndex);
        GraphqlUseCase mGetHomePageData = new GraphqlUseCase();
        mGetHomePageData.clearRequest();
        //Adding request for main query
        Map<String, Object> variablesMain = new HashMap<>();
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE, currentPageIndex);
        variablesMain.put(CommonConstant.GraphqlVariableKeys.PAGE_SIZE, CommonConstant.PAGE_SIZE);

        GraphqlRequest graphqlRequestMain = new GraphqlRequest(GraphqlHelper.loadRawString(mContext.getResources(), R.raw.tp_gql_point_history),
                PointHistoryBase.class,
                variablesMain, false);
        mGetHomePageData.addRequest(graphqlRequestMain);


        mGetHomePageData.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loadCompletedWithError();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                PointHistoryBase data = graphqlResponse.getData(PointHistoryBase.class);

                if (data != null) {
                    loadCompleted(data.getPointHistory().getItems(), data);
                    setLastPage(!data.getPointHistory().getPageInfo().isHasNext());
                } else {
                    loadCompletedWithError();
                }
            }
        });
    }
}
