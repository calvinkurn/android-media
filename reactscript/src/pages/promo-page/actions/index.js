import axios from 'axios'
import { NetworkModule } from 'NativeModules'



const BASE_API_URL = {
    mojito_prod: 'https://mojito.tokopedia.com/os/api/v1/ospromo',
    mojito_staging: 'https://mojito-staging.tokopedia.com/os/api/v1/ospromo',
}


// =================== Reload State =================== //
export const RELOADSTATE = 'RELOADSTATE'
export const reloadState = () => {
    return {
        type: RELOADSTATE
    }
}


// =================== Fetch Banner =================== //
export const FETCH_TOPBANNER = 'FETCH_TOPBANNER'
export const fetchTopBanner = (dataSlug) => {
    return {
        type: FETCH_TOPBANNER,
        payload: fetchBanners(dataSlug)
    }
}

fetchBanners = async (dataSlug) => {
    const url = `${BASE_API_URL.mojito_prod}/topcontent/${dataSlug.slug}?device=mobile`
    return NetworkModule.getResponse(url, "GET", '', true)
        .then(response => {
            let jsonResponse = JSON.parse(response)
            return jsonResponse
        })
        .catch(error => {
            return { data: [] }
        })
}


// =================== Fetch Products Categories =================== //
export const FETCH_CATEGORIES = 'FETCH_CATEGORIES'
export const fetchCategories = (dataSlug, offset, limit) => {
    return {
        type: FETCH_CATEGORIES,
        payload: fetcProdCategories(dataSlug, offset, limit)
    }
}

fetcProdCategories = async (dataSlug, offset, limit) => {
    const url = `${BASE_API_URL.mojito_prod}/categories?promo=${dataSlug.slug}&device=mobile&limit=${limit}&offset=${offset}`
    return NetworkModule.getResponse(url, 'GET', '', true)
        .then(res => {
            let jsonResponse = JSON.parse(res)
            console.log(jsonResponse)
            return jsonResponse
        })
        .catch(err => {
            return { data: [] }
        })
}