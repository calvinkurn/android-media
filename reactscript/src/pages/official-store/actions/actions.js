import axios from 'axios'
import find from 'lodash/find'

const MOJITO_HOSTNAME = 'https://mojito.tokopedia.com'
const TOME_HOSTNAME = 'https://tome.tokopedia.com'

const endpoints = {
  campaign: `${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/campaigns?device=:device&full_domain=:domain&image_size=:imageSize&image_square=:imageSquare`,
  banners: `${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/banners?device=2`
}

export const FETCH_CAMPAIGNS = 'FETCH_CAMPAIGNS'
export const fetchCampaigns = () => {
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
    return axios.get(url)
  }

  const getCampaigns = () => {
    return axios.get(url)
      .then(response => {
        const campaigns = response.data.data.campaigns
        return getBanners()
          .then(res => {
            const banners = res.data.data.banners
            const promoBanner = find(banners, { html_id: 6 })
            if (promoBanner) {
              promoBanner.Products = []
              campaigns.splice(2, 0, promoBanner)
            }
            return {
              data: campaigns
            }
          })
          .catch(err => {
            console.log(err)
          })
      })
      .catch(error => {
        console.log(error)
      })
  }

  return {
    type: FETCH_CAMPAIGNS,
    payload: getCampaigns()
  }
}

export const FETCH_BANNERS = 'FETCH_BANNERS'
export const fetchBanners = () => ({
  type: FETCH_BANNERS,
  payload: axios.get(`${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/banners?device=2`)
})

export const FETCH_BRANDS = 'FETCH_BRANDS'
export const fetchBrands = (limit, offset) => ({
  type: FETCH_BRANDS,
  payload: axios.get(`${MOJITO_HOSTNAME}/os/api/v1/brands/list?device=lite&microsite=true&user_id=0&limit=${limit}&offset=${offset}`)
    .then(response => {
      const brands = response.data.data
      const total_brands = response.data.total_brands
      let shopList = brands.map(shop => ({
        id: shop.shop_id,
        name: shop.shop_name,
        brand_img_url: shop.brand_img_url,
        logo_url: shop.logo_url,
        microsite_url: shop.microsite_url,
        shop_mobile_url: shop.shop_mobile_url,
        shop_domain: shop.shop_domain,
        isFav: false,
      }))
      let shopIds = brands.map(shop => shop.shop_id)
      shopIds = shopIds.toString()
      const shopCount = shopIds.length

      const url = `${MOJITO_HOSTNAME}/os/api/v1/brands/microsite/products?device=lite&source=osmicrosite&rows=4&full_domain=tokopedia.lite:3000&ob=11&image_size=200&image_square=true&brandCount=${shopCount}&brands=${shopIds}`
      console.log(url)
      return axios.get(`${url}`)
        .then(response => response.data.data.brands)
        .then(brandsProducts => {
          shopList = shopList.map(shop => {
            const shopProduct = find(brandsProducts, product => {
              return product.brand_id === shop.id
            })

            if (shopProduct && shopProduct.data) {
              shop.products = shopProduct.data.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price,
                image_url: product.image_url,
                is_wishlist: false,
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
          return {
            data: shopList,
            total_brands,
          }
        })
    })
})

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

export const ADD_TO_WISHLIST = 'ADD_TO_WISHLIST'
export const addToWishlist = (productId) => ({
  type: ADD_TO_WISHLIST,
  payload: productId
})

export const REMOVE_FROM_WISHLIST = 'REMOVE_FROM_WISHLIST'
export const removeFromWishlist = (productId) => ({
  type: REMOVE_FROM_WISHLIST,
  payload: productId
})

export const REMOVE_FROM_FAVOURITE = 'REMOVE_FROM_FAVOURITE'
export const removeFromFavourite = (shopId) => ({
  type: REMOVE_FROM_FAVOURITE,
  payload: shopId,
})

export const ADD_TO_FAVOURITE = 'ADD_TO_FAVOURITE'
export const addToFavourite = (shopId) => ({
  type: ADD_TO_FAVOURITE,
  payload: shopId,
})
/*
export const addToFavourite = (userId, shopId, sessionId) => {
  const sidCookie = `_SID_Tokopedia_Coba_=${sessionId}`
  const config = {
    url: `${TOME_HOSTNAME}/shop/favorite-shop`,
    method: 'POST',
    headers: {
      origin: `https://m.tokopedia.com`,
      referer: `https://m.tokopedia.com`,
      'Content-Type': 'application/x-www-form-urlencoded',
      Cookie: sidCookie,
      Authorization: undefined,
    },
    data: {
      user_id: userId,
      s_id: shopID,
      ad_key: null,
      action: 'fav_shop',
      act: 'POST'
    }
  }

  return {
    type: ADD_TO_FAVOURITE,
    payload: axios(config)
      .then(response => {
        console.log(response)
      })
      .catch(error => {
        console.log(error)
      })
  }
}
*/



