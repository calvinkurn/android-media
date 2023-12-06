package com.tokopedia.linter.detectors

import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import com.android.tools.lint.detector.api.XmlScannerConstants.ALL
import com.tokopedia.linter.detectors.xml.XMLDetector
import org.w3c.dom.Attr

class TkpdDesignAttributeDetector : Detector(), XmlScanner {

    val listLayout = listOf(
        "bottomsheetbuilder_list_divider",
        "branding_layout",
        "closeable_bottom_sheet_dialog",
        "design_text_input_password_icon",
        "dialog_base",
        "dialog_calendar",
        "dialog_longprominance",
        "hint_text_input_layout",
        "item_autocomplete_text_double_row",
        "item_custom_quick_filter_view",
        "item_custom_rounded_filter_view",
        "item_quick_filter_view",
        "item_selection_label_view",
        "item_shimmering_grid",
        "item_shimmering_list",
        "partial_shimmering_grid",
        "partial_shimmering_grid_list_horizontal",
        "partial_shimmering_label_view",
        "partial_shimmering_list",
        "permission_fragment",
        "rounded_closeable_bottom_sheet",
        "rounded_closeable_bottom_sheet_dialog",
        "tkpd_hint_text_input_layout_test",
        "widget_bottom_sheet",
        "widget_bottomsheet",
        "widget_custom_quick_filter",
        "widget_decimal_range_input_view",
        "widget_deletable_item_view",
        "widget_label_view",
        "widget_menu",
        "widget_menu_item",
        "widget_menu_item_action",
        "widget_menu_item_title",
        "widget_quick_filter",
        "widget_rounded_corner_filter",
        "widget_selection_label_view"
    )


    val listDrawable = listOf(
        "background_countdown",
        "background_countdown_unify",
        "bg_button_counter_minus",
        "bg_button_counter_minus_disabled",
        "bg_button_counter_minus_enabled",
        "bg_button_counter_plus",
        "bg_button_counter_plus_disabled",
        "bg_button_counter_plus_enabled",
        "bg_button_disabled",
        "bg_button_green",
        "bg_button_green_border",
        "bg_button_green_enabled",
        "bg_button_green_enabled_border",
        "bg_button_orange",
        "bg_button_orange_enabled",
        "bg_button_white",
        "bg_button_white_border",
        "bg_button_white_enabled",
        "bg_button_white_enabled_border",
        "bg_button_white_transparent",
        "bg_button_white_transparent_enabled",
        "bg_green_border_rect",
        "bg_green_rounded_tradein",
        "bg_green_toolbar_drop_shadow",
        "bg_header_rounded_closeable_bs",
        "bg_line_separator_thin",
        "bg_list_separator",
        "bg_radio_button",
        "bg_radio_button_tkpddesign",
        "bg_rect_white_round",
        "bg_round_corner",
        "bg_round_corner_solid_green",
        "bg_round_corner_solid_grey",
        "bg_rounded_chat_input",
        "bg_rounded_corner_white",
        "bg_rounded_corners_green_with_shadow",
        "bg_rounded_corners_red",
        "bg_rounder_black",
        "bg_search_input_text_area",
        "bg_single_line",
        "bg_switch_thumb_selector",
        "bg_switch_track_off",
        "bg_switch_track_on",
        "bg_switch_track_selector",
        "bg_used_voucher",
        "border_bottom_no_rounded",
        "bottom_sheet_shadow",
        "btn_bg_rounded_corners",
        "btn_cancel",
        "button_border_green",
        "button_buy_from_cart",
        "button_curvy_green",
        "card_shadow_bottom",
        "card_shadow_top",
        "chip_filter_selected",
        "chip_filter_unselected",
        "circle_red",
        "close_button_bottom_sheet",
        "color_state_disable_text",
        "deletable_item_background",
        "deletable_item_button",
        "divider_horizontal_custom_quick_filter",
        "error_drawable",
        "green_radio_button",
        "green_radio_selected",
        "green_radio_unselected",
        "header_bottom_sheet_rounded_white",
        "ic_arrow_down_grey",
        "ic_arrow_drop_down_grey_checkout_module",
        "ic_arrow_right_default",
        "ic_arrow_right_grey",
        "ic_arrow_up_grey",
        "ic_badge_free_return",
        "ic_badge_gold",
        "ic_badge_shop_official",
        "ic_btn_fb",
        "ic_btn_g",
        "ic_btn_sms",
        "ic_btn_twitter",
        "ic_calendar_grey",
        "ic_check",
        "ic_close_default",
        "ic_close_thin",
        "ic_close_white",
        "ic_dot_black",
        "ic_download",
        "ic_empty_search",
        "ic_empty_state",
        "ic_error_to_send",
        "ic_icon_alert",
        "ic_icon_back_black",
        "ic_icon_lock",
        "ic_info",
        "ic_info_black",
        "ic_loading_image",
        "ic_lock",
        "ic_ovo_logo_purple",
        "ic_plus_add",
        "ic_radiobutton_normal",
        "ic_radiobutton_selected",
        "ic_search_grey",
        "ic_search_icon",
        "ic_send_black_24dp",
        "ic_share_grey",
        "ic_share_white",
        "ic_smiley_bad",
        "ic_smiley_good",
        "ic_smiley_neutral",
        "ic_switch_thumb_on",
        "ic_switch_tumb_off",
        "ic_three_dots_more_menu",
        "ic_ticker_action_close_12dp",
        "ic_wishlist_checked",
        "ic_wishlist_unchecked",
        "loading_page",
        "loading_page_tkpddesign",
        "order_filter_selector_chip",
        "price_input_seekbar_button_pressed",
        "rect_white_rounded_stroke_gray",
        "rect_white_stroke_gray",
        "tkpd_rating_bar"

    )

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.DRAWABLE
    }

    override fun getApplicableAttributes(): Collection<String>? {
        return ALL
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val attributeValue = attribute.nodeValue
        if (listLayout.contains(
                attributeValue.substringAfter(
                    "@layout/"
                )
            ) || listDrawable.contains(
                attributeValue.substringAfter(
                    "@drawable/"
                )
            )
        ) {
            val message =
                "Detected using tkpddesign or ambiguous with tkpddesign. Using component from the tkpddesign package is not allowed. Because TkpdDesign will be deleted soon. You can also consult @edwinnrw or @faisalramd"
            context.report(ISSUE, attribute, context.getLocation(attribute), message)
        }
    }

    companion object {
        val ISSUE = Issue.create(
            "TkpdDesignResourceUsage",
            "Avoid using tkpddesign",
            "Detected using tkpddesign or ambiguity with tkpddesign. Using component from the tkpddesign package is not allowed. Because TkpdDesign will be deleted soon. You can also consult @edwinnrw or @faisalramd",
            Category.CORRECTNESS, 6, Severity.ERROR,
            Implementation(TkpdDesignAttributeDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )
    }
}
