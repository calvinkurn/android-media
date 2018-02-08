import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from '../store/Store'
import DetailCicilan from '../components/DetailCicilan'


class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <DetailCicilan />
            </Provider>
        )
    }
}
export default Root