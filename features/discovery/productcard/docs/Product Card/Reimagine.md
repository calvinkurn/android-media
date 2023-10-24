---
labels:
- product-card
---


| **Status**      | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                  |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------|
| Team            | Minion Dave                                                                                                                        |
| Module type     | <!--start status:GREY-->SUB-FEATURE<!--end status-->                                                                               |
| Release date    | 6 Oct 2023 / MA-3.238                                                                                                              |
| Product Manager | @Gilang                                                                                                                            |
| Contributors    | `@Zulfikar Rahman @Darian Thedy @Willybrodus Purnama`                                                                              |
| Module Location | `features/discovery/productcard` / `com.tokopedia.productcard.reimagine`                                                           |
| Product PRD     | [Product Card Reimagine](https://tokopedia.atlassian.net/wiki/spaces/SE/pages/2285208035/H2+2023+Product+Card+-+ReImagine+Version) |

<!--toc-->

## Overview

### Background

Product card is getting longer. The more information we provide in product card, the more decision fatigue for our buyer. Hence, we need to show only the important information that help on purchase decision making

Currently, product card is using the masonry layout (lego-like layout). On this layout, each product card will have dynamic position based on the product card height. Because of that, it's quite hard for our buyer to compare the information since they cannot see the product card information side-by-side easily.

### Project Description

The goal of a product card reimagine is to provide potential buyers with all the necessary information about a particular product in a clear, concise, and compact manner, so that buyer is able to decide what product to purchase. Moreover, the slot on product card is very limited, we only show the relevant information to the buyer.

From tech side, based on previous experience, now we have decided to split Product Card into multiple template or layout based on how it will be used. Different use cases will have different components inside the Product Card, for example Product Card in regular grid or list layout and Product Card in carousel layout will have different size and different labels. If a new component is added into the Product Card, we must specify in which template it will be added into, and develop only in that template.

This approach has a benefit of not cluttering all Product Card that is used in all pages with new labels or new components. If a component is only used in regular grid Product Card, it will not exists in carousel Product Card. However, this approach also have a downside where if a new component is needed in all templates, then we have to develop multiple times for all the templates.

For all templates, we will still use the same `ProductCardModel`. These are the current templates of the Product Card:

| Template       | Release Status    | Class                       |
|----------------|-------------------|-----------------------------|
| Grid (regular) | Released MA-3.240 | ProductCardGridView         |
| Grid Carousel  | Released MA-3.238 | ProductCardGridCarouselView |
| Grid Small     | Not Released      | -                           |
| Grid (regular) | Not Released      | -                           |
| Grid Carousel  | Not Released      | -                           |
| Grid Small     | Not Released      | -                           |


#### Product Card Grid
![](../res/reimagine_grid.png)

#### Product Card Grid Carousel
![](../res/reimagine_grid_carousel.png)


#### Image
Image is rendered from field `imageUrl`.

#### IsAds
Ads marker on the bottom right of the image is rendered from field `isAds`. 

#### Name
Name is rendered from field `name`.

#### Price
Price is rendered from field `price`.

#### Slashed Price
Slashed Price / Original Price before discount is rendered with a strikethrough from field `slashedPrice`.

#### Discount Percentage
Discount Percentage is rendered from field `discountPercentage`.

#### Rating
Rating is rendered as text beside a star is rendered from field `rating`.

#### Shop Badge
Shop Badge is rendered from field `title` and `imageUrl`. `title` is usually filled with shop location (for non-OS) or shop name (for OS), and it is mandatory field to render the shop info. `imageUrl` is an image for the shop badge, and it is not necessary to render the shop info. 

#### Free Shipping
Free Shipping or Bebas Ongkir logo is rendered from field `freeShipping.imageUrl`.

#### Add to Cart
Add to Cart button is shown if `hasAddToCart` is set to true. It is also required to implement `setAddToCartOnClickListener` and call Add to Cart GQL from the implementer.

#### Video URL
Video Identifier will be shown if the `videoUrl` is not empty, and Video Sneakpeek of the product will be played with the `videoUrl`.

#### Three Dots
Three dots will be shown if `hasThreeDots` is set to true. It is also required to implement `setThreeDotsClickListener` and call `ProductCardOptionsManager.showProductCardOptions` to open the product card options bottomsheet.

#### Stock Info
Stock Info progress bar is rendered from field `label`, `percentage`, and `labelColor`. Progress bar color will change depending on the `label`, and it can also show fire icon if the `label` is `"Segera Habis"`.

#### Label Group - Benefit
Label Benefit is rendered if `labelGroupList` contains a label group with `position` = `ri_product_benefit`. The text will be rendered from the label group's `title`, and the label background will be rendered based on the label group's type. This is usually `lightGreen` for this label.

#### Label Group - Credibility
Label Credibility is rendered beside rating if `labelGroupList` contains a label group with `position` = `ri_product_credibility`. The text will be rendered from the label group's `title`. This is usually said `xx+ terjual` (sold count).

#### Label Group - Ribbon
Label Ribbon is rendered on the top left corner of the product card if `labelGroupList` contains a label group with `position` = `ri_ribbon`. The text will be rendered from label group's `title`. This is usually filled with Discount percentage.

## How to
To implement Product Card Reimagine, choose a template that you needed, and implement the class mentioned above in your XML. After that, create an instance of `ProductCardModel` and call `setProductModel`.


## Action Items

