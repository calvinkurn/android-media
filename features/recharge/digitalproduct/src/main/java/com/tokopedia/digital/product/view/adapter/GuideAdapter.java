package com.tokopedia.digital.product.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.activity.DigitalYoutubeActivity;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.unifycomponents.LoaderUnify;
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant;

import java.util.ArrayList;
import java.util.List;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * @author by furqan on 04/07/18.
 */

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {

    private static final int LAYOUT = R.layout.view_digital_item_guide_tab;

    private List<GuideData> guideDataList = new ArrayList<>();
    private Context context;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;

    public GuideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public GuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuideViewHolder(LayoutInflater.from(context)
                .inflate(LAYOUT, parent, false));
    }

    @Override
    public void onBindViewHolder(GuideViewHolder holder, int position) {
        GuideData data = guideDataList.get(position);

        holder.tvTitle.setText(data.getTitle());
        holder.youtubeTV.initialize(YoutubePlayerConstant.GOOGLE_API_KEY,
                youtubeListenerInitialize(data.getSourceLink(), holder.progressBar));
        holder.youtubeTV.setOnClickListener(youtubeListenerOnClick(data.getSourceLink()));
    }

    @Override
    public int getItemCount() {
        return guideDataList.size();
    }

    public void addData(List<GuideData> guideDataList) {
        if (guideDataList == null) return;
        this.guideDataList.addAll(guideDataList);
        notifyDataSetChanged();
    }

    private YouTubeThumbnailView.OnInitializedListener youtubeListenerInitialize(String youtubeViewUrl, LoaderUnify progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        return new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader loader) {
                youTubeThumbnailLoader = loader;
                loader.setVideo(youtubeViewUrl);

                loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        loader.release();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        youtubeListenerInitialize(youtubeViewUrl, progressBar);
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                progressBar.setVisibility(View.GONE);
                youtubeListenerInitialize(youtubeViewUrl, progressBar);
            }
        };
    }

    private View.OnClickListener youtubeListenerOnClick(String videoId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        DigitalYoutubeActivity.createInstance(context, videoId)
                );
            }
        };
    }

    class GuideViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        YouTubeThumbnailView youtubeTV;
        LoaderUnify progressBar;

        public GuideViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_guide_title);
            youtubeTV = itemView.findViewById(R.id.youtube_thumbnail_view);
            itemView.findViewById(R.id.circle_thumbnail_view)
                    .setBackground(MethodChecker
                            .getDrawable(itemView.getContext(),
                                    R.drawable.digital_ic_play_circle_outline_48dp));

            progressBar = itemView.findViewById(R.id.youtube_thumbnail_loading_bar);
        }
    }

}
