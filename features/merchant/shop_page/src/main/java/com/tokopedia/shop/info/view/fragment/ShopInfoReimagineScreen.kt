package com.tokopedia.shop.info.view.fragment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.digitsOnly
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.shop.R
import com.tokopedia.shop.info.domain.entity.ShopEpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopOperationalHour
import com.tokopedia.shop.info.domain.entity.ShopPerformance
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.view.custom.ShopReviewView
import com.tokopedia.shop.info.view.model.ShopInfoPreviewParameterProvider
import com.tokopedia.shop.info.view.model.ShopInfoUiState

@Composable
fun ShopInfoScreen(uiState: ShopInfoUiState) {
    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = stringResource(id = R.string.shop_info_shop_profile),
                onBackClicked = {
                   
                }
            )
        )
    }) { paddingValues ->
        if (uiState.isLoading) {
            LoadingContent()
        } else {
            Content(modifier = Modifier.padding(paddingValues), uiState)    
        }
        
    }

}

@Composable
fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        NestLoader(
            modifier = Modifier.align(Alignment.Center),
            variant = NestLoaderType.Circular(size = NestLoaderSize.Small)
        )
    }
}


@Composable
fun Content(modifier: Modifier = Modifier, uiState: ShopInfoUiState) {
    val scrollState = rememberScrollState()
    
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ShopCoreInfo(
            shopImageUrl = uiState.shopImageUrl,
            shopBadgeUrl = uiState.shopBadgeUrl,
            shopName = uiState.shopName
        )

        val hasMainLocation = uiState.mainLocation.isNotEmpty()
        val hasOperationalHour = uiState.operationalHours.isNotEmpty()
        val hasJoinDate = uiState.shopJoinDate.isNotEmpty()
        val showShopInfo = hasMainLocation || hasOperationalHour || hasJoinDate
        
        if (showShopInfo) {
            Spacer(modifier = Modifier.height(16.dp))
            ShopInfo(
                mainLocation = uiState.mainLocation,
                otherLocation = uiState.otherLocation,
                operationalHours = uiState.operationalHours,
                shopJoinDate = uiState.shopJoinDate
            )
        }
        
        if (uiState.showEpharmacyInfo) {
            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = NestTheme.colors.NN._50
            )
            Spacer(modifier = Modifier.height(16.dp))
            ShopEpharmacyInfo(epharmacy = uiState.epharmacy)
        }
        
        val hasRating = uiState.rating.detail.isNotEmpty()
        val hasReview = uiState.review.reviews.isNotEmpty()
        
        if (hasRating || hasReview) {
            Spacer(modifier = Modifier.height(8.dp))
            ShopRatingAndReviews(uiState.rating, uiState.review)
        }

        Spacer(modifier = Modifier.height(16.dp))
        ShopPerformance(uiState.shopPerformance)
       
        if (uiState.shopNotes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            ShopNotes(uiState.shopNotes)
        }
    
        if (uiState.shopDescription.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ShopDescription(uiState.shopDescription)
        }
        
        if (uiState.shipments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ShopSupportedShipment(uiState.shipments)
        }
        
        ReportShop()
    }
}

@Composable
fun ShopCoreInfo(shopImageUrl: String, shopBadgeUrl: String, shopName: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        NestImage(
            modifier = Modifier.size(52.dp),
            source = ImageSource.Remote(source = shopImageUrl),
            type = NestImageType.Circle
        )
        Spacer(modifier = Modifier.width(12.dp))
        
        if (shopBadgeUrl.isNotEmpty()) {
            NestImage(
                modifier = Modifier.size(12.dp),
                source = ImageSource.Remote(source = shopBadgeUrl)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
      
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = shopName,
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            ),
            maxLines = 1
        )
    }
}
@Composable
fun ShopInfo(
    mainLocation: String,
    otherLocation: String,
    operationalHours: Map<String, String>,
    shopJoinDate: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Informasi Toko",
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        
        if (mainLocation.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            ShopLocation(mainLocation, otherLocation)
        }
        
        if (operationalHours.isNotEmpty()){
            Spacer(modifier = Modifier.height(12.dp))
            ShopOperationalHour(operationalHours)
        }
        
        if (shopJoinDate.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            ShopJoinDate(shopJoinDate)
        }
    }
}

@Composable
fun ShopLocation(mainLocation: String, otherLocation: String) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Lokasi",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom)  {
        NestTypography(
            text = mainLocation,
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )

        if (otherLocation.isNotEmpty()) {
            Spacer(modifier = Modifier.width(2.dp))
            NestTypography(
                text = otherLocation,
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.NN._600
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            NestIcon(
                modifier = Modifier.size(18.dp),
                iconId = IconUnify.CHEVRON_DOWN,
                colorLightEnable = NestTheme.colors.NN._900,
                colorNightEnable = NestTheme.colors.NN._900
            )
        }
    }
}
@Composable
fun ShopOperationalHour(operationalHours: Map<String, String>) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Jam Operasional",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(4.dp))
    ShopOperationalHoursList(operationalHours)
}

@Composable
fun ShopOperationalHoursList(operationalHours: Map<String, String>) {
    operationalHours.forEach { operationalHour ->
        Row {
            NestTypography(
                text = operationalHour.key + ":",
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.NN._600
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            NestTypography(
                text = operationalHour.value,
                textStyle = NestTheme.typography.display2.copy(
                    fontWeight = FontWeight.Bold,
                    color = NestTheme.colors.NN._950
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun ShopJoinDate(shopJoinDate: String) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Tanggal Bergabung",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(4.dp))
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = shopJoinDate,
        textStyle = NestTheme.typography.display2.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950
        )
    )
}

@Composable
fun ShopEpharmacyInfo(epharmacy: ShopEpharmacyInfo) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ShopEpharmacyNearestPickup(epharmacy.nearestPickupAddress)
        
        Spacer(modifier = Modifier.height(8.dp))
        ShopEpharmacyNearestPickupLocationCta(epharmacy.nearPickupAddressAppLink)
        
        Spacer(modifier = Modifier.height(12.dp))
        ShopEpharmacyPharmacistOperationalHour(epharmacy.pharmacistOperationalHour)
        
        Spacer(modifier = Modifier.height(12.dp))
        ShopEpharmacyPharmacistName(epharmacy.pharmacistName)
        
        Spacer(modifier = Modifier.height(12.dp))
        ShopEpharmacySiaNumber(epharmacy.siaNumber)
    }
}


@Composable
fun ShopEpharmacyNearestPickup(nearestPickupAddress: String) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Lokasi pickup terdekat",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(2.dp))
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = nearestPickupAddress,
        textStyle = NestTheme.typography.display2.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950
        )
    )
}

@Composable
fun ShopEpharmacyNearestPickupLocationCta(appLink: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        NestIcon(
            modifier = Modifier.size(16.dp),
            iconId = IconUnify.MAP,
            colorLightEnable = NestTheme.colors.GN._500,
            colorNightEnable = NestTheme.colors.GN._500
        )
        Spacer(modifier = Modifier.width(4.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Lihat Lokasi",
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.GN._500
            )
        )
    }
}
@Composable
fun ShopEpharmacyPharmacistOperationalHour(pharmacistOperationalHour: String) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Jam Kerja Apoteker:",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(4.dp))
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = pharmacistOperationalHour,
        textStyle = NestTheme.typography.display2.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950
        )
    )
}

@Composable
fun ShopEpharmacyPharmacistName(pharmacistName: String) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Apotek Jaga:",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(4.dp))
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = pharmacistName,
        textStyle = NestTheme.typography.display2.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950
        )
    )
}

@Composable
fun ShopEpharmacySiaNumber(siaNumber: String) {
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = "Nomor SIA:",
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600
        )
    )
    Spacer(modifier = Modifier.height(4.dp))
    NestTypography(
        modifier = Modifier.fillMaxWidth(),
        text = siaNumber,
        textStyle = NestTheme.typography.display2.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950
        )
    )
}

@Composable
fun ShopRatingAndReviews(rating: ShopRating, review: ShopReview) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Ulasan buat toko ini",
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        ShopRatingAndReviewRecap(rating, review)
        
        if (rating.detail.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ShopRating(rating)
        }
     

        if (review.reviews.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ShopReview(review)
        }
    }
}

@Composable
fun ShopRatingAndReviewRecap(rating: ShopRating, review: ShopReview) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (rating.ratingScore.isNotEmpty()) {
            NestIcon(
                modifier = Modifier.size(16.dp),
                iconId = IconUnify.STAR_FILLED,
                colorLightEnable = NestTheme.colors.YN._300,
                colorNightEnable = NestTheme.colors.YN._600
            )
            Spacer(modifier = Modifier.width(4.dp))
            NestTypography(
                text = rating.ratingScore,
                textStyle = NestTheme.typography.display2.copy(
                    fontWeight = FontWeight.Bold,
                    color = NestTheme.colors.NN._950
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        
        val ratingAndReviewText = when {
            rating.totalRating.isMoreThanZero() && review.totalReviews.isMoreThanZero() -> {
                "${rating.totalRatingFmt} rating • ${review.totalReviews.splitByThousand()} ulasan"
            }
            rating.totalRating.isMoreThanZero() -> {
                "${rating.totalRatingFmt} rating"
            }
            review.totalReviews.isMoreThanZero() -> {
                "${review.totalReviews} ulasan"
            }
            else -> ""
        }
        
        val formattedRatingAndReviewText = "($ratingAndReviewText)"
        NestTypography(
            text = formattedRatingAndReviewText,
            textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
        )
    }
}


@Composable
fun ShopRating(rating: ShopRating) {
    val cornerShapeRadiusDp = 12.dp
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .background(
                    color = NestTheme.colors.NN._50,
                    shape = RoundedCornerShape(cornerShapeRadiusDp)
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NestTypography(
                text = "${rating.positivePercentageFmt.digitsOnly()}%",
                textStyle = NestTheme.typography.heading1.copy(color = NestTheme.colors.NN._950)
            )

            NestTypography(
                text = "pembeli puas",
                textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            rating.detail.forEach { rating -> ShopRatingBarItem(rating = rating) }
        }
    }
}
@Composable
fun ShopReview(review: ShopReview) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NestTheme.colors.NN._300, RoundedCornerShape(12.dp))
            .padding(16.dp),
        factory = { context ->
            ShopReviewView(context)
        },
        update = {
            it.render(review)
        }
    )
}

@Composable
fun ShopPerformance(shopPerformance: ShopPerformance) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Performa toko",
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(modifier = Modifier.fillMaxWidth()){
            Row(modifier = Modifier.fillMaxWidth()) {
                ShopPerformanceMetricItem(
                    modifier = Modifier.weight(1f),
                    metricName = "Produk terjual",
                    metricValue = shopPerformance.totalProductSoldCount.ifEmpty { "-" }
                )
                ShopPerformanceMetricItem(
                    modifier = Modifier.weight(1f),
                    metricName = "Performa chat",
                    metricValue = shopPerformance.chatPerformance.ifEmpty { "-" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            ShopPerformanceMetricItem(
                metricName = "Pesanan diproses",
                metricValue = shopPerformance.orderProcessTime.ifEmpty { "-" }
            )
        }
    }
}
@Composable
fun ShopNotes(shopNotes: List<ShopNote>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Catatan Toko",
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        shopNotes.forEach { shopNote -> ShopNoteItem(shopNote = shopNote) }
    }
}
@Composable
fun ShopDescription(shopDescription: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Deskripsi Toko",
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = shopDescription,
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._950
            )
        )
    }
}

@Composable
fun ShopSupportedShipment(shipments: List<ShopSupportedShipment>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Dukungan Pengiriman",
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = "Durasi dihitung sejak barang diserahkan ke kurir",
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._600
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        shipments.forEach { shipment -> 
            ShopShipmentItem(shipment = shipment)
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = NestTheme.colors.NN._50
            )
        }
    }
}

@Composable
fun ReportShop() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NestTypography(
            text = "Toko bermasalah?",
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.NN._950
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        NestTypography(
            text = "Laporkan",
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.GN._500
            )
        )
    }
}

@Composable
fun ShopPerformanceMetricItem(modifier: Modifier = Modifier, metricValue: String, metricName: String) {
    Column(modifier = modifier) {
        NestTypography(
            text = metricValue,
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )
        NestTypography(
            text = metricName,
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._600
            )
        )
    }
}

@Composable
fun ShopNoteItem(shopNote: ShopNote) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            NestTypography(
                modifier = Modifier.weight(1f),
                text = shopNote.title,
                textStyle = NestTheme.typography.display3.copy(
                    fontWeight = FontWeight.Bold,
                    color = NestTheme.colors.NN._950
                )
            )
            NestIcon(modifier = Modifier.size(16.dp), iconId = IconUnify.CHEVRON_RIGHT)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = NestTheme.colors.NN._50
        )
    }
}

@Composable
fun ShopShipmentItem(shipment: ShopSupportedShipment) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        NestImage(
            modifier = Modifier.size(48.dp),
            source = ImageSource.Remote(source = shipment.imageUrl),
            type = NestImageType.Rect()
        )
        Column {
            NestTypography(
                text = shipment.title,
                textStyle = NestTheme.typography.display2.copy(
                    color = NestTheme.colors.NN._950
                )
            )
            NestTypography(
                text = shipment.serviceNames.joinToString(separator = ",") { it },
                textStyle = NestTheme.typography.paragraph3.copy(
                    color = NestTheme.colors.NN._600
                )
            )
        }
    }
 
}

@Composable
fun ShopRatingBarItem(rating: ShopRating.Detail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestIcon(
            modifier = Modifier.size(16.dp),
            iconId = IconUnify.STAR_FILLED,
            colorLightEnable = NestTheme.colors.YN._300,
            colorNightEnable = NestTheme.colors.YN._600
        )
        Spacer(modifier = Modifier.width(4.dp))
        NestTypography(
            text = rating.rate.toString(),
            textStyle = NestTheme.typography.display2.copy(color = NestTheme.colors.NN._950)
        )
        Spacer(modifier = Modifier.width(4.dp))
        
        val progressBarBackgroundColor = NestTheme.colors.NN._50
        LinearProgressIndicator(
            progress = (rating.percentageFloat.toFloat() / 100),
            modifier = Modifier
                .weight(2f)
                .height(4.dp)
                .background(progressBarBackgroundColor, RoundedCornerShape(8.dp)),
            color = NestTheme.colors.YN._300,
            backgroundColor = progressBarBackgroundColor
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        NestTypography(
            modifier = Modifier.weight(1f),
            text = rating.formattedTotalReviews,
            textStyle = NestTheme.typography.display2.copy(color = NestTheme.colors.NN._950)
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ShopInfoScreenPreview(
    @PreviewParameter(ShopInfoPreviewParameterProvider::class) uiState: ShopInfoUiState
) {
    NestTheme {
        ShopInfoScreen(uiState)
    }
}
