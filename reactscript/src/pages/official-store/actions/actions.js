import find from 'lodash/find'
import { NetworkModule, NavigationModule } from 'NativeModules'

const MOJITO_HOSTNAME = 'https://mojito.tokopedia.com'
const TOME_HOSTNAME = 'https://tome.tokopedia.com'

const endpoints = {
    campaign: `${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/campaigns?device=:device&full_domain=:domain&image_size=:imageSize&image_square=:imageSquare`,
    banners: `${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/banners?device=2`,
    checkWishlist: `${MOJITO_HOSTNAME}/v1/users/:id/wishlist/check/:list_id`,
}


// ========================= Reload State ========================= //
export const RELOADSTATE = 'RELOADSTATE'
export const reloadState = (state, action) => {
    return {
        type: RELOADSTATE
    }
}

// ========================= Check Product in Wishlist ========================= //
const checkProductInWishlist = (userId, pIds) => {
    if (userId == 0) {
        return Promise.resolve({
            data: {
                data: { ids: [] }
            }
        })
    }

    const url = endpoints.checkWishlist
        .replace(':id', userId)
        .replace(':list_id', pIds)

    return NetworkModule.getResponse(url, "GET", "", true)
        .then(response => {
            const jsonResponse = JSON.parse(response)
            return jsonResponse
        })
        .catch(error => { })
}



// ========================= Fetch Campaigns ========================= //
export const FETCH_CAMPAIGNS = 'FETCH_CAMPAIGNS'
export const fetchCampaigns = (User_ID) => {
    const device = 'lite'
    const imageSize = 200
    const imageSquare = true
    const domain = 'm.tokopedia.com'

    const url = endpoints.campaign.replace(':device', device)
        .replace(':domain', domain)
        .replace(':imageSize', imageSize)
        .replace(':imageSquare', imageSquare)

    const getBanners = () => {
        const url = endpoints.banners
        return NetworkModule.getResponse(url, "GET", "", true)
    }

    const getCampaigns = (User_ID) => {
        return NetworkModule.getResponse(url, "GET", "", true)
            .then(response => {
                const jsonResponseCampaigns = JSON.parse(response)
                const campaigns = jsonResponseCampaigns.data.campaigns
                return getBanners()
                    .then(res => {
                        const jsonResponseBanners = JSON.parse(res)
                        const banners = jsonResponseBanners.data.banners
                        const promoBanner = find(banners, { html_id: 6 })
                        if (promoBanner) {
                            promoBanner.Products = []
                            campaigns.splice(2, 0, promoBanner)
                        }
                        
                        const pIds = []
                        campaigns.forEach(c => {
                            const products = c.Products
                            products.forEach(product => {
                                pIds.push(product.data.id)
                            })
                        })
                        
                        let wishlistProd = []
                        let URL_wishlist = `${MOJITO_HOSTNAME}/v1/users/` + User_ID + `/wishlist/check/` + pIds.toString();
                        return NetworkModule.getResponse(URL_wishlist, "GET", '', true)
                            .then(response => {
                                let jsonResponse = JSON.parse(response)
                                wishlistProd = jsonResponse.data.ids.map(id => +id)
                                return {
                                    data: campaigns.map(c => {
                                        return {
                                            ...c,
                                            Products: c.Products.map(p => {
                                            const is_wishlist = User_ID === '' ? (false) : (wishlistProd.indexOf(p.data.id) > -1 ? true : false)
                                            // const is_wishlist = wishlistProd.indexOf(p.data.id) > -1 ? true : false
                                            return {
                                                ...p,
                                                data: {
                                                    ...p.data,
                                                    is_wishlist
                                                }
                                            }
                                        })
                                    }
                                })
                            }
                        })
                        .catch(error => {
                            return {
                                data: campaigns.map(c => {
                                    return {
                                        ...c,
                                        Products: c.Products.map(p => {
                                            const is_wishlist = wishlistProd.indexOf(p.data.id) > -1 ? true : false
                                            return {
                                                ...p,
                                                data: {
                                                    ...p.data,
                                                    is_wishlist,
                                                }
                                            }
                                        })
                                    }
                                })
                            }
                        })
                    })
                    .catch(err => {
                        return {
                            data: campaigns
                        }
                    })
                })
                .catch(error => {
                    return {
                        data: []
                    }
                })
        }

    return {
        type: FETCH_CAMPAIGNS,
        payload: getCampaigns(User_ID)
    }
}



// ========================= Fetch Banners ========================= //
export const FETCH_BANNERS = 'FETCH_BANNERS'
export const fetchBanners = () => ({
    type: FETCH_BANNERS,
    payload: NetworkModule.getResponse(`${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/banners?device=2`, "GET", '', true)
        .then(response => {
            let jsonResponse = JSON.parse(response);
            const banners = jsonResponse.data.banners || []
            return { data: banners }
        })
        .catch(error => {
            return { data: [] }
        })
})



// ========================= Fetch Brands ========================= //
export const RESET_BRANDS = 'RESET_BRANDS'
export const resetBrands = (limit, offset, User_ID, status) => ({
  type: RESET_BRANDS,
  payload: getBrands(limit, offset, User_ID, status)
})


export const FETCH_BRANDS = 'FETCH_BRANDS'
export const fetchBrands = (limit, offset, User_ID, status) => ({
    type: FETCH_BRANDS,
    payload: getBrands(limit, offset, User_ID, status)
})


getBrands = (limit, offset, User_ID, status) => {
    const Check_UserID = !User_ID ? 0 : User_ID
    const URL_ = `${MOJITO_HOSTNAME}/os/api/v1/brands/list?device=lite&microsite=true&user_id=${Check_UserID}&limit=${limit}&offset=${offset}`
    return NetworkModule.getResponse(URL_, "GET", "", false)
        .then(response => {
            const jsonResponse = JSON.parse(response)
            const brands = jsonResponse.data
            const total_brands = jsonResponse.total_brands
            let shopList = brands.map(shop => ({
                id: shop.shop_id,
                name: shop.shop_name,
                brand_img_url: shop.brand_img_url,
                logo_url: shop.logo_url,
                microsite_url: shop.microsite_url,
                shop_mobile_url: shop.shop_mobile_url,
                shop_apps_url: shop.shop_apps_url,
                shop_domain: shop.shop_domain,
                isFav: false,
            }))
            let shopIds = brands.map(shop => shop.shop_id)
            shopIds = shopIds.toString()
            const shopCount = shopIds.length
            const url = `${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/products?device=lite&source=osmicrosite&rows=4&full_domain=tokopedia.lite:3000&ob=11&image_size=200&image_square=true&brandCount=${shopCount}&brands=${shopIds}`
            let ids = []
            let wishlistProd = []
            return NetworkModule.getResponse(url, "GET", "", false)
                .then(brandsProducts => {
                    const jsonResponse = JSON.parse(brandsProducts)
                    const brands = jsonResponse.data.brands
                    shopList = shopList.map(shop => {
                        const shopProduct = find(brands, product => {
                            return product.brand_id === shop.id
                        })
                        if (shopProduct && shopProduct.data) {
                            shopProduct.data.map(product => {
                                ids.push(product.id)
                            })
                            shop.products = shopProduct.data.map(product => ({
                                id: product.id,
                                name: product.name,
                                price: product.price,
                                image_url: product.image_url,
                                is_wishlist: true,
                                url_app: product.url_app,
                                url: product.url,
                                shop_name: product.shop.name,
                                labels: product.labels,
                                badges: product.badges,
                                discount_percentage: product.discount_percentage,
                                original_price: product.original_price,
                            }))
                            
                            return shop
                        } else {
                            shop.products = []
                            return shop
                        }
                    })
  
            if (Check_UserID !== 0){
                return checkProductInWishlist(Check_UserID, ids.toString())
                    .then(res => {
                        wishlistProd = res.data.ids.map(id => +id)

                        return {
                            data: shopList.map(s => {
                                return {
                                    ...s,
                                    products: s.products.map(p => {
                                        return {
                                            ...p,
                                            is_wishlist: wishlistProd.indexOf(p.id) > -1 ? true : false
                                        }
                                    })
                                }
                            }),
                            total_brands,
                            // status: !status ? null : status
                        }
                    })
                    .catch(err => { })
                } else {
                    return {
                        data: shopList.map(s => {
                            return {
                                ...s,
                                products: s.products.map(p => {
                                    return {
                                        ...p,
                                        is_wishlist: false
                                    }
                                })
                            }
                        }),
                        total_brands,
                        // status: !status ? null : status   
                    }
                }

            })
        })
    }



// ========================= Slide Brands ========================= //
export const SLIDE_BRANDS = 'SLIDE_BRANDS'
export const slideBrands = () => ({
    type: SLIDE_BRANDS
})

function getProductIdList(products) {
    const productIdList = []
    products.forEach((product) => {
        productIdList.push(product.data.map(p => p.id))
    })
    const pIds = []
    productIdList.forEach((p) => {
        p.forEach(o => {
            pIds.push(o)
        })
    })
    return pIds
}



// ========================= Add to Wishlist ========================= //
export const ADD_TO_WISHLIST = 'ADD_TO_WISHLIST'
export const addToWishlist = (productId, User_ID) => {
    const device = 'lite'
    const imageSize = 200
    const imageSquare = true
    const domain = 'm.tokopedia.com'

    const url = endpoints.campaign.replace(':device', device)
        .replace(':domain', domain)
        .replace(':imageSize', imageSize)
        .replace(':imageSquare', imageSquare)

    const fetchDatas = () => {
        return NetworkModule.getResponse(url, "GET", "", true)
            .then(response => {
                const jsonResponse = JSON.parse(response)
                const campaigns = jsonResponse.data.campaigns
                NetworkModule.getResponse("https://mojito.tokopedia.com/users/" + User_ID + "/wishlist/" + productId + "/v1.1", "POST", "{}", true)
                    .then(responseWishlist => console.log("Success AddToWishlist"))
                    .catch(err => console.log(err))
                return { campaigns, productId }
            })
            .catch(error => {
                return { data: [] }
            })
    };

    return {
        type: ADD_TO_WISHLIST,
        payload: fetchDatas()
    }
}


// ========================= Remove from Wishlist ========================= //
export const REMOVE_FROM_WISHLIST = 'REMOVE_FROM_WISHLIST'
export const removeFromWishlist = (productId, User_ID) => {
    const removeData = () => {
        NetworkModule.getResponse("https://mojito.tokopedia.com/users/" + User_ID + "/wishlist/" + productId + "/v1.1", "DELETE", "{}", true)
            .then(response => console.log(response))
            .catch(error => console.log(error))
        return productId
    }

    return {
        type: REMOVE_FROM_WISHLIST,
        payload: removeData()
    }
}



// ========================= Add to Favorite ========================= //
export const ADD_TO_FAVOURITE = 'ADD_TO_FAVOURITE'
export const addToFavourite = (shopId, User_ID) => ({
    type: ADD_TO_FAVOURITE,
    payload: NetworkModule.getResponse("https://ws.tokopedia.com/v4/action/favorite-shop/fav_shop.pl", "POST", '{ "shop_id": ' + shopId + ' }', true)
        .then(response => {
            let jsonResponse = JSON.parse(response)
            return shopId
        })
        .catch(error => { })
})


// ========================= Remove From Favorite ========================= //
export const REMOVE_FROM_FAVOURITE = 'REMOVE_FROM_FAVOURITE'
export const removeFromFavourite = (shopId, User_ID) => ({
    type: REMOVE_FROM_FAVOURITE,
    payload: NetworkModule.getResponse("https://ws.tokopedia.com/v4/action/favorite-shop/fav_shop.pl", "POST", '{ "shop_id": ' + shopId + ' }', true)
        .then(response => {
            let jsonResponse = JSON.parse(response)
            return shopId
        })
        .catch(error => { })
})


// ========================= Refresh State ========================= //
export const REFRESH_STATE = 'REFRESH_STATE'
export const refreshState = () => ({
    type: REFRESH_STATE
})





// ========================= Add Wishlist from PDP | Campaign ========================= //
export const ADD_TO_WISHLIST_PDP_CAMPAIGN = 'ADD_TO_WISHLIST_PDP_CAMPAIGN'
export const addWishlistFromPdp = res => ({
    type: ADD_TO_WISHLIST_PDP_CAMPAIGN,
    payload: res
})

export const REMOVE_WISHLIST_PDP_CAMPAIGN = 'REMOVE_WISHLIST_PDP_CAMPAIGN'
export const removeWishlistFromPdp = res => ({
    type: REMOVE_WISHLIST_PDP_CAMPAIGN,
    payload: res
})


// ========================= Favorite from PDP | Brands ============================ //
export const ADD_TO_FAVOURITE_PDP = 'ADD_TO_FAVOURITE_PDP'
export const addToFavouritePdp = shopId => ({
    type: ADD_TO_FAVOURITE_PDP,
    payload: shopId
})

export const REMOVE_FROM_FAVOURITE_PDP = 'REMOVE_FROM_FAVOURITE_PDP'
export const removeFavoritePdp = shopId => ({
    type: REMOVE_FROM_FAVOURITE_PDP,
    payload: shopId
})


// ========================= Wishlist from PDP | Brands ============================ //
export const ADD_TO_WISHLIST_BRAND_PDP = 'ADD_TO_WISHLIST_BRAND_PDP'
export const addWishlistFromPdpBrands = res => ({
    type: ADD_TO_WISHLIST_BRAND_PDP,
    payload: res
})

export const REMOVE_WISHLIST_BRAND_PDP = 'REMOVE_WISHLIST_BRAND_PDP'
export const removeWishlistPdpBrands = res => ({
    type: REMOVE_WISHLIST_BRAND_PDP,
    payload: res
})