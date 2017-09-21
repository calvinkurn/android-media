import React, { Component } from 'react'
import { DeviceEventEmitter } from 'react-native'
import { connect } from 'react-redux'
import { fetchCampaigns, addWishlistFromPdp, removeWishlistFromPdp } from '../actions/actions'
import CampaignList from '../components/campaignList'
import { NavigationModule } from 'NativeModules';


class CampaignContainer extends Component {
    componentDidMount() {
        const { dispatch } = this.props

        NavigationModule.getCurrentUserId().then(uuid => {
            dispatch(fetchCampaigns(uuid))
        })
        
        this.addToWishlist = DeviceEventEmitter.addListener("WishlistAdd", (res) => {
            dispatch(addWishlistFromPdp(res))
        });
        this.removeWishlist = DeviceEventEmitter.addListener("WishlistRemove", (res) => {
            dispatch(removeWishlistFromPdp(res))
        });
        this.checkLoginCampaign = DeviceEventEmitter.addListener('Login', (res) => {
            dispatch(fetchCampaigns(res.user_id))
        })
    }

    componentWillUnmount(){
        this.addToWishlist.remove()
        this.removeWishlist.remove()
        this.checkLoginCampaign.remove()
    }

    render() {
        const campaigns = this.props.campaigns.items
        return (
          this.props.campaigns.isFetching ? null : <CampaignList campaigns={campaigns} />
        )
    }
}

const mapStateToProps = state => {
    const campaigns = state.campaigns
    return {
        campaigns
    }
}

export default connect(mapStateToProps)(CampaignContainer)