package com.tokopedia.flashsale.management.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.DRAFT;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.FINISHED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.IN_REVIEW;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.IN_SUBMISSION;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.ONGOING_CANCELLED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.ON_GOING;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.PUBLISHED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.PUBLISH_CANCELLED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.READY;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.READY_CANCELLED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.READY_LOCKED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.READY_LOCKED_CANCELLED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.REVIEW_CANCELLED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.SCHEDULED;
import static com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef.SUBMISSION_CANCELLED;

@Retention(RetentionPolicy.SOURCE)
@IntDef({SCHEDULED, DRAFT, PUBLISHED, IN_SUBMISSION, IN_REVIEW, READY, ON_GOING,
        FINISHED, PUBLISH_CANCELLED, SUBMISSION_CANCELLED, REVIEW_CANCELLED, READY_CANCELLED,
        ONGOING_CANCELLED, READY_LOCKED, READY_LOCKED_CANCELLED})
public @interface FlashSaleCampaignStatusIdTypeDef {
    int SCHEDULED = 1;
    int DRAFT = 2;
    int PUBLISHED = 3;
    int IN_SUBMISSION = 4;
    int IN_REVIEW = 5;
    int READY = 6;
    int ON_GOING = 7;
    int FINISHED = 8;
    int PUBLISH_CANCELLED = 9;
    int SUBMISSION_CANCELLED = 10;
    int REVIEW_CANCELLED = 11;
    int READY_CANCELLED = 12;
    int ONGOING_CANCELLED = 13;
    int READY_LOCKED = 14;
    int READY_LOCKED_CANCELLED = 15;
}
